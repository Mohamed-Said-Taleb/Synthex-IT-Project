package com.devsling.fr.service.Impl;

import com.devsling.fr.controller.AuthController;
import com.devsling.fr.dto.GetForgetPasswordResponse;
import com.devsling.fr.dto.GetTokenValidationResponse;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.ForgetPasswordToken;
import com.devsling.fr.repository.ForgetPasswordRepository;
import com.devsling.fr.service.ForgetPasswordService;
import com.devsling.fr.service.UserService;
import com.devsling.fr.tools.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgetPasswordImpl implements ForgetPasswordService {

    private final JavaMailSender javaMailSender;
    private final ForgetPasswordRepository forgetPasswordRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Override
    public void saveForgetPasswordToken(ForgetPasswordToken token) {
        forgetPasswordRepository.save(token);
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }



    @Override
    public LocalDateTime expireTimeRange() {
        return LocalDateTime.now().plusMinutes(Constants.MINUTES);
    }

    @Override
    public ForgetPasswordToken getByToken(String token) {
        return forgetPasswordRepository.findByToken(token);
    }

    @Override
    public void sendMail(String receiver, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message=  javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        String emailContent ="<p>Hello</p>"+"Click the link bellow to reset password"
                +"<p><a href=\""+ emailLink + "\">Change my password</p>";

        helper.setText(emailContent,true);
        helper.setFrom("mohamed.s.taleb@devsling.com","Coding Support");
        helper.setSubject(subject);
        helper.setTo(receiver);
        javaMailSender.send(message);

    }
    @Override
    public GetTokenValidationResponse resetPasswordAndValidateToken(String token, String password, String confirmationPassword) {
        try {
            if (password.equals(confirmationPassword)) {
                ForgetPasswordToken forgetPasswordToken = getByToken(token);
                if (forgetPasswordToken != null && !forgetPasswordToken.isUsed() && !isExpired(forgetPasswordToken)) {
                    AppUser user = forgetPasswordToken.getAppUser();
                    forgetPasswordToken.setUsed(true);
                    user.setPassword(bCryptPasswordEncoder.encode(password));
                    userService.saveUser(user);
                    saveForgetPasswordToken(forgetPasswordToken);
                    return new GetTokenValidationResponse("Password reset successful");
                } else {
                    return new GetTokenValidationResponse("Invalid or used/expired token");
                }
            } else {
                return new GetTokenValidationResponse("Password and confirmation password do not match");
            }
        } catch (Exception e) {
            log.error("Error resetting password and validating token", e);
            return new GetTokenValidationResponse("Error resetting password");
        }
    }

    @Override
    public boolean isExpired(ForgetPasswordToken forgetPasswordToken) {
        return LocalDateTime.now().isAfter(forgetPasswordToken.getExpireTime());
    }

    public GetForgetPasswordResponse passwordReset(String email) {
        try {
            AppUser user = userService.findUserByEmail(email);
            if (user == null) {
                return new GetForgetPasswordResponse("This email is not registered",null);
            }

            ForgetPasswordToken forgetPasswordToken = createForgetPasswordToken(user);


            sendMail(user.getEmail(), "Password reset link", "link");

            saveForgetPasswordToken(forgetPasswordToken);

            log.info("Password reset email sent for user: {}", user.getUsername());

            return new GetForgetPasswordResponse("Password reset email sent successfully",forgetPasswordToken.getToken());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending password reset email", e);
            return new GetForgetPasswordResponse("Error sending password reset email",null);
        }
    }
    private ForgetPasswordToken createForgetPasswordToken(AppUser user) {
        return ForgetPasswordToken.builder()
                .expireTime(expireTimeRange())
                .isUsed(false)
                .appUser(user)
                .token(generateToken())
                .build();
    }


}
