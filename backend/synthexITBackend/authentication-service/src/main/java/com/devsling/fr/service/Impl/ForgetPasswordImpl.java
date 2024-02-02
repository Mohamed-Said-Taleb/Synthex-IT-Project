package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.ForgetPasswordToken;
import com.devsling.fr.repository.ForgetPasswordRepository;
import com.devsling.fr.service.ForgetPasswordService;
import com.devsling.fr.service.UserService;
import com.devsling.fr.service.helper.Helper;
import com.devsling.fr.tools.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.devsling.fr.tools.Constants.EMAIL_PERSONAL;
import static com.devsling.fr.tools.Constants.EMAIL_SENDER;

@Service
@RequiredArgsConstructor
public class ForgetPasswordImpl implements ForgetPasswordService {

    private final JavaMailSender javaMailSender;
    private final ForgetPasswordRepository forgetPasswordRepository;
    private final UserService userService;
    private final Helper helper;
    private final TemplateEngine thymeleafTemplateEngine;

    public static final String EMAIL_LINK = "emaillink";
    public static final String USERNAME = "username";
    public static final String EMAIL_TEMPLATE = "reset-password-email-template";




    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public ForgetPasswordToken getByToken(String token) {
        return forgetPasswordRepository.findByToken(token);
    }

    @Override
    public void sendMail(String username,String receiver, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Context context = new Context();
        context.setVariable(EMAIL_LINK, emailLink);
        context.setVariable(USERNAME, username);
        String emailContent = thymeleafTemplateEngine.process(EMAIL_TEMPLATE, context);
        helper.setText(emailContent, true);
        helper.setFrom(new InternetAddress(EMAIL_SENDER, EMAIL_PERSONAL));
        helper.setSubject(subject);
        helper.setTo(receiver);
        javaMailSender.send(message);
    }

    @Override
    public GetTokenValidationResponse validatePasswordReset(String token, String password, String confirmationPassword) {
        try {
            if (password.equals(confirmationPassword)) {
                ForgetPasswordToken forgetPasswordToken = getByToken(token);
                if (helper.isValidToken(forgetPasswordToken)) {
                    AppUser user = forgetPasswordToken.getAppUser();
                    helper.resetPasswordAndSave(forgetPasswordToken, user, password);
                    return new GetTokenValidationResponse("Password reset successful");
                } else {
                    return new GetTokenValidationResponse("Invalid or used/expired token");
                }
            } else {
                return new GetTokenValidationResponse("Password and confirmation password do not match");
            }
        } catch (Exception e) {
            return new GetTokenValidationResponse("Error resetting password");
        }
    }
    @Override
    public GetForgetPasswordResponse passwordResetMail(String email) {
        try {
            GetForgetPasswordResponse validationResponse = helper.validatePasswordReset(email);
            if (!validationResponse.getMessage().equals("Validation successful")) {
                return validationResponse;
            }

            AppUser user = userService.findUserByEmail(email);
            ForgetPasswordToken forgetPasswordToken = createForgetPasswordToken(user);

            sendMail(user.getUsername(), user.getEmail(), "Password reset link", "link");

            helper.saveForgetPasswordToken(forgetPasswordToken);
            return new GetForgetPasswordResponse("Password reset email sent successfully", forgetPasswordToken.getToken());
        } catch (MessagingException | UnsupportedEncodingException e) {
            return new GetForgetPasswordResponse("Error sending password reset email", null);
        }}

    private ForgetPasswordToken createForgetPasswordToken(AppUser user) {
        return ForgetPasswordToken.builder().expireTime(helper.expireTimeRange()).isUsed(false).appUser(user).token(generateToken()).build();
    }
}
