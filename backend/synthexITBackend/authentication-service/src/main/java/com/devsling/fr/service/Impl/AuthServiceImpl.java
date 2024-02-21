package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Requests.CandidateRequest;
import com.devsling.fr.dto.Requests.EmployerCreateRequest;
import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.dto.Responses.VerificationResponse;
import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.ForgetPasswordToken;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.JwtUtils;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.EmailSenderService;
import com.devsling.fr.service.UserService;
import com.devsling.fr.service.helper.Helper;
import com.devsling.fr.service.out.CandidateApiClient;
import com.devsling.fr.service.out.EmployerApiClient;
import com.devsling.fr.tools.Constants;
import com.devsling.fr.tools.RoleName;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.devsling.fr.tools.Constants.WRONG_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final Helper helper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CandidateApiClient candidateApiClient;
    private final EmployerApiClient employerApiClient;
    private final EmailSenderImpl emailSender;
    private final EmailSenderService emailSenderService;
    private final UserService userService;

    public static final String  VERIFICATION_EMAIL_TEMPLATE = "verification-email-template";
    public static final String  SUBJECT = "Activate your SynthexIT account";
    public static final String  ACTIVATE_ACCOUNT_PATH = "/activate-account";

    public static final String FORGET_PASSWORD_EMAIL_TEMPLATE = "reset-password-email-template";


    @Override
    public Mono<RegisterResponse> signup(SignUpFormRequest signUpFormRequest) {
        RegisterResponse validationResponse = helper.validateSignUpFormRequest(signUpFormRequest);
        if (Constants.SUCCESS_VALIDATION.equals(validationResponse.getMessage())) {
            if (signUpFormRequest.getRole_Name() != null) {

                AppUser appUserBd = AppUser.builder()
                        .username(signUpFormRequest.getUsername())
                        .email(signUpFormRequest.getEmail())
                        .password(bCryptPasswordEncoder.encode(signUpFormRequest.getPassword()))
                        .gender(signUpFormRequest.getGender())
                        .enabled(false)
                        .verificationCode(RandomString.make(64))
                        .gender(signUpFormRequest.getGender())
                        .appRoles(Collections.singletonList(AppRole.builder()
                                .role(signUpFormRequest.getRole_Name())
                                .build()))
                        .build();

                if (appUserBd.getAppRoles().get(0).getRole().equals(RoleName.CANDIDATE.name())) {
                    CandidateRequest candidateRequest = CandidateRequest.builder()
                            .lastName(appUserBd.getUsername())
                            .email(appUserBd.getEmail())
                            .firstName(signUpFormRequest.getFirstName())
                            .resumeUrl(signUpFormRequest.getResumeUrl())
                            .professionalExperiences(signUpFormRequest.getProfessionalExperiences())
                            .build();
                    return candidateApiClient.saveCandidate(candidateRequest)
                            .flatMap(candidateCreateResponse -> {
                                userRepository.save(appUserBd);
                                emailSender.sendMail(appUserBd.getUsername(),
                                        appUserBd.getEmail(),
                                        SUBJECT ,
                                        appUserBd.getVerificationCode(),
                                        VERIFICATION_EMAIL_TEMPLATE,
                                        ACTIVATE_ACCOUNT_PATH
                                        );

                                return Mono.just(RegisterResponse.builder()
                                        .message(Constants.REGISTRATION_CANDIDATE_MESSAGE)
                                        .build());
                            })
                            .onErrorResume(throwable -> Mono.just(RegisterResponse.builder()
                                    .message("Error creating candidate: " + throwable.getMessage())
                                    .build()));
                }
                if (appUserBd.getAppRoles().get(0).getRole().equals(RoleName.EMPLOYER.name())) {

                    EmployerCreateRequest employerCreateRequest = EmployerCreateRequest.builder()
                            .lastName(appUserBd.getUsername())
                            .email(appUserBd.getEmail())
                            .build();
                    userRepository.save(appUserBd);
                    return employerApiClient.saveEmployer(employerCreateRequest)
                            .flatMap(candidateCreateResponse -> {
                                userRepository.save(appUserBd);
                                emailSender.sendMail(appUserBd.getUsername(),
                                        appUserBd.getEmail(),
                                        SUBJECT,
                                        appUserBd.getVerificationCode(),
                                        VERIFICATION_EMAIL_TEMPLATE,
                                        ACTIVATE_ACCOUNT_PATH);
                                return Mono.just(RegisterResponse.builder()
                                        .message(Constants.REGISTRATION_EMPLOYER_MESSAGE)
                                        .build());
                            })
                            .onErrorResume(throwable -> Mono.just(RegisterResponse.builder()
                                    .message("Error creating candidate: " + throwable.getMessage())
                                    .build()));
                } else {
                    return Mono.just(RegisterResponse.builder()
                            .message("User cannot be registered as a candidate")
                            .build());
                }
            } else {
                return Mono.just(RegisterResponse.builder()
                        .message("Role should be specified")
                        .build());
            }
        } else {
            return Mono.just(RegisterResponse.builder()
                    .message("Error registering user: " + validationResponse.getMessage())
                    .build());
        }
    }
    @Override
    public Mono<GetTokenResponse> getToken(LoginFormRequest loginFormRequest) {
        RegisterResponse validationResponse = helper.validateLoginFormRequest(loginFormRequest);

        if (!"Validation successful".equals(validationResponse.getMessage())) {
            return Mono.just(GetTokenResponse.builder()
                    .message(validationResponse.getMessage())
                    .build());
        }

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginFormRequest.getUsername(), loginFormRequest.getPassword())
            );

            if (authenticate.isAuthenticated()) {
                Map<String,String> idToken = jwtUtils.generateToken(loginFormRequest.getUsername(), authenticate);
                return Mono.just(GetTokenResponse.builder()
                        .token(idToken.get(Constants.ACCEESS_TOKEN))
                        .refreshToken(idToken.get(Constants.REFRESH_TOKEN))
                        .message("Authentication successful")
                        .build());
            } else {
                return Mono.just(GetTokenResponse.builder()
                        .message("Invalid access")
                        .build());
            }
        } catch (AuthenticationException e) {
            return Mono.just(GetTokenResponse.builder()
                    .message("Invalid username or password")
                    .build());
        }
    }
    @Override
    public Mono<GetTokenValidationResponse> validateToken(String token) {
        boolean isTokenValid = jwtUtils.validateToken(token);

        if (isTokenValid) {
            return Mono.just(GetTokenValidationResponse
                    .builder()
                    .message(Constants.VALID_TOKEN)
                    .build());
        } else {
            return Mono.just(GetTokenValidationResponse
                    .builder()
                    .message(Constants.INVALID_TOKEN)
                    .build());
        }
    }
    @Override
    public Mono<VerificationResponse> verifyAccountWithEmail(String token) {
        return Mono.defer(() -> {
            Optional<AppUser> optionalUser = userRepository.findByVerificationCode(token);
            if (optionalUser.isPresent()) {
                AppUser user = optionalUser.get();
                user.setEnabled(true);
                userRepository.save(user);
                return Mono.just(VerificationResponse.builder()
                        .message(Constants.SUCCESS_VERIFICATION)
                        .build());
            } else {
                return Mono.just(VerificationResponse.builder()
                        .message(Constants.FAILED_VERIFICATION)
                        .build());
            }
        });
    }

    @Override
    public  Mono<GetForgetPasswordResponse> passwordResetMail(String email) throws MessagingException, UnsupportedEncodingException {
        GetForgetPasswordResponse validationResponse = helper.validatePasswordReset(email);
        if (!validationResponse.getMessage().equals("Validation successful")) {
            return Mono.just(validationResponse);
        }

        Optional<AppUser> user = userService.findUserByEmail(email);
        ForgetPasswordToken forgetPasswordToken = createForgetPasswordToken(user.get());

        emailSenderService.sendMail(user.get().getUsername(),
                user.get().getEmail(),
                "Password reset link",
                forgetPasswordToken.getToken(),
                FORGET_PASSWORD_EMAIL_TEMPLATE,
                "reset-password");

        helper.saveForgetPasswordToken(forgetPasswordToken);
        return Mono.just(new GetForgetPasswordResponse("Password reset email sent successfully", forgetPasswordToken.getToken())) ;
    }
    private ForgetPasswordToken createForgetPasswordToken(AppUser user) {
        return ForgetPasswordToken.builder().expireTime(helper.expireTimeRange()).isUsed(false).appUser(user).token(emailSenderService.generateToken()).build();
    }

    @Override
    public Mono<GetTokenValidationResponse> validatePasswordReset(String token, String password, String confirmationPassword) {
        try {
            if (password.equals(confirmationPassword)) {
                ForgetPasswordToken forgetPasswordToken = emailSenderService.getByToken(token);
                if (helper.isValidToken(forgetPasswordToken)) {
                    if(helper.isStrongerPassword(password)){
                        helper.resetPasswordAndSave(forgetPasswordToken,forgetPasswordToken.getAppUser(), password);
                        return  Mono.just(new GetTokenValidationResponse(WRONG_PASSWORD));
                    }else {
                        return Mono.just(GetTokenValidationResponse.builder()
                                .message("Password reset successful")
                                .build());
                    }

                } else {
                    return Mono.just(GetTokenValidationResponse.builder()
                            .message("Invalid or used/expired token")
                            .build());
                }
            } else {
                return Mono.just(GetTokenValidationResponse.builder()
                        .message("Password and confirmation password do not match")
                        .build());
            }
        } catch (Exception e) {
            return Mono.just(GetTokenValidationResponse.builder()
                    .message("Error resetting password")
                    .build());}
    }
}
