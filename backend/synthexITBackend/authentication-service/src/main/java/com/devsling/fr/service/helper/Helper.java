package com.devsling.fr.service.helper;

import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.ForgetPasswordToken;
import com.devsling.fr.repository.MailSenderRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.service.UserService;
import com.devsling.fr.tools.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.devsling.fr.tools.Constants.ENABLED_ACCOUNT;

@Service
@RequiredArgsConstructor
public class Helper {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailSenderRepository mailSenderRepository;


    public Boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public GetForgetPasswordResponse validatePasswordReset(String email) {
        Optional<AppUser> user = userService.findUserByEmail(email);

        if (user == null) {
            return new GetForgetPasswordResponse("This email is not registered", null);
        }

        return new GetForgetPasswordResponse("Validation successful", null);
    }

    public RegisterResponse validateSignUpFormRequest(SignUpFormRequest signUpFormRequest) {
        if (userRepository.existsByUsername(signUpFormRequest.getUsername())) {
            return RegisterResponse
                    .builder()
                    .message("Username is already in use")
                    .build();
        }


        if (signUpFormRequest.getUsername().isEmpty()) {
            return RegisterResponse.builder().message("Username should not be empty").build();
        }

        if (signUpFormRequest.getPassword().isEmpty()) {
            return RegisterResponse.builder().message("Password should not be empty").build();
        }

        if (!isStrongerPassword(signUpFormRequest.getPassword())) {
            return RegisterResponse.builder().message("Weak Password: Password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character.").build();
        }

        if (signUpFormRequest.getEmail().isEmpty()) {
            return RegisterResponse.builder().message("Email should not be empty").build();
        }

        if (!isValidEmailAddress(signUpFormRequest.getEmail())) {
            return RegisterResponse.builder().message("Invalid email").build();
        }

        if (userRepository.existsByEmail(signUpFormRequest.getEmail())) {
            return RegisterResponse.builder().message("Email is already in use").build();
        }
        return RegisterResponse.builder().message("Validation successful").build();
    }

    public RegisterResponse validateLoginFormRequest(LoginFormRequest loginFormRequest) {
        if (loginFormRequest.getUsername() == null || loginFormRequest.getUsername().isEmpty()) {
            return RegisterResponse.builder().message("Username should not be empty").build();
        }

        if (loginFormRequest.getPassword() == null || loginFormRequest.getPassword().isEmpty()) {
            return RegisterResponse.builder().message("Password should not be empty").build();
        }

        Optional<AppUser> user = userRepository.findByUsername(loginFormRequest.getUsername());

        if(user.isPresent()){
            if (!user.get().isEnabled()) {
                return RegisterResponse
                        .builder()
                        .message(ENABLED_ACCOUNT)
                        .build();
            }
        }
        return RegisterResponse.builder().message("Validation successful").build();
    }

    public boolean isStrongerPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            return false;
        }

        if (!containsUppercaseLetter(password)) {
            return false;
        }

        if (!containsLowercaseLetter(password)) {
            return false;
        }

        if (!containsDigit(password)) {
            return false;
        }

        return containsSpecialCharacter(password);
    }

    public boolean isValidToken(ForgetPasswordToken forgetPasswordToken) {
        return forgetPasswordToken != null && !forgetPasswordToken.isUsed() ;
    }

    public boolean isExpired(ForgetPasswordToken forgetPasswordToken) {
        return LocalDateTime.now().isAfter(forgetPasswordToken.getExpireTime());
    }

    public LocalDateTime expireTimeRange() {
        return LocalDateTime.now().plusMinutes(Constants.MINUTES);
    }

    public void resetPasswordAndSave(ForgetPasswordToken forgetPasswordToken, AppUser user, String password) {
        forgetPasswordToken.setUsed(true);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userService.saveUser(user);
        saveForgetPasswordToken(forgetPasswordToken);
    }
    public void saveForgetPasswordToken(ForgetPasswordToken token) {
        mailSenderRepository.save(token);
    }

    private boolean containsUppercaseLetter(String password) {
        return password.matches(".*[A-Z].*");
    }

    private boolean containsLowercaseLetter(String password) {
        return password.matches(".*[a-z].*");
    }

    private boolean containsDigit(String password) {
        return password.matches(".*\\d.*");
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches(".*[@$!%*?&].*");
    }
}
