package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.ForgetPasswordToken;
import com.devsling.fr.repository.MailSenderRepository;
import com.devsling.fr.service.EmailSenderService;
import com.devsling.fr.service.UserService;
import com.devsling.fr.service.helper.Helper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.devsling.fr.tools.Constants.EMAIL_PERSONAL;
import static com.devsling.fr.tools.Constants.EMAIL_SENDER;
import static com.devsling.fr.tools.Constants.WRONG_PASSWORD;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final MailSenderRepository mailSenderRepository;
    private final UserService userService;
    private final Helper helper;
    private final TemplateEngine thymeleafTemplateEngine;

    public static final String EMAIL_LINK = "emailVerificationLink";
    public static final String USERNAME = "username";
    public static final String FORGET_PASSWORD_EMAIL_TEMPLATE = "reset-password-email-template";

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderImpl.class);

    //should change this to ip address of the deployment
    public static final String  LINK = "http://localhost:8080/auth/";
    public static final String  TOKEN = "?token=";



    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public ForgetPasswordToken getByToken(String token) {
        return mailSenderRepository.findByToken(token);
    }

    @Override
    public void sendMail(String username, String receiver, String subject, String emailLink, String emailTemplate,String api) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            Context context = new Context();
            context.setVariable(EMAIL_LINK, LINK+api+TOKEN+emailLink);
            context.setVariable(USERNAME, username);
            String emailContent = thymeleafTemplateEngine.process(emailTemplate, context);
            helper.setText(emailContent, true);
            helper.setFrom(new InternetAddress(EMAIL_SENDER, EMAIL_PERSONAL));
            helper.setSubject(subject);
            helper.setTo(receiver);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Error occurred while sending email: {}", e.getMessage(), e);
        }
    }

    @Override
    public Mono<GetTokenValidationResponse> validatePasswordReset(String token, String password, String confirmationPassword) {
        try {
            if (password.equals(confirmationPassword)) {
                ForgetPasswordToken forgetPasswordToken = getByToken(token);
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
    @Override
    public  Mono<GetForgetPasswordResponse> passwordResetMail(String email) {
        GetForgetPasswordResponse validationResponse = helper.validatePasswordReset(email);
        if (!validationResponse.getMessage().equals("Validation successful")) {
            return Mono.just(validationResponse);
        }

        AppUser user = userService.findUserByEmail(email);
        ForgetPasswordToken forgetPasswordToken = createForgetPasswordToken(user);

        sendMail(user.getUsername(),
                user.getEmail(),
                "Password reset link",
                forgetPasswordToken.getToken(),
                FORGET_PASSWORD_EMAIL_TEMPLATE,
                "reset-password");

        helper.saveForgetPasswordToken(forgetPasswordToken);
        return Mono.just(new GetForgetPasswordResponse("Password reset email sent successfully", forgetPasswordToken.getToken())) ;
    }

    private ForgetPasswordToken createForgetPasswordToken(AppUser user) {
        return ForgetPasswordToken.builder().expireTime(helper.expireTimeRange()).isUsed(false).appUser(user).token(generateToken()).build();
    }
}
