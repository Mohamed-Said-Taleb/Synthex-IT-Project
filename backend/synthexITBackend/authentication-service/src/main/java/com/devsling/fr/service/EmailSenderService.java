package com.devsling.fr.service;

import com.devsling.fr.entities.ForgetPasswordToken;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailSenderService {



    String generateToken();

    ForgetPasswordToken getByToken(String token);
    void sendMail(String username,String to,String object,String emailLink,String emailTemplate,String api) throws MessagingException, UnsupportedEncodingException;
}
