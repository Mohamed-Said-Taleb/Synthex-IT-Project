package com.devsling.fr.service;

import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.entities.ForgetPasswordToken;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;

public interface ForgetPasswordService {

    GetTokenValidationResponse validatePasswordReset(String token, String password, String confirmationPassword);

    GetForgetPasswordResponse passwordResetMail(String email);

    void saveForgetPasswordToken(ForgetPasswordToken token);
    String generateToken();

    ForgetPasswordToken getByToken(String token);
    void sendMail(String username,String to,String object,String emailLink) throws MessagingException, UnsupportedEncodingException;
}
