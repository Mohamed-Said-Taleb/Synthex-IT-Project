package com.devsling.fr.service;

import com.devsling.fr.entities.ForgetPasswordToken;
import com.devsling.fr.repository.MailSenderRepository;
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

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.devsling.fr.tools.Constants.EMAIL_PERSONAL;
import static com.devsling.fr.tools.Constants.EMAIL_SENDER;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final MailSenderRepository mailSenderRepository;
    private final TemplateEngine thymeleafTemplateEngine;

    public static final String EMAIL_LINK = "emailVerificationLink";
    public static final String USERNAME = "username";

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
}
