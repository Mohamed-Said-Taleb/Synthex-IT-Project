package com.devsling.fr.service;

import com.devsling.fr.dto.GetForgetPasswordResponse;
import com.devsling.fr.entities.ForgetPasswordToken;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import com.devsling.fr.dto.GetTokenValidationResponse;

public interface ForgetPasswordService {

    GetTokenValidationResponse resetPasswordAndValidateToken(String token, String password, String confirmationPassword);

    boolean isExpired(ForgetPasswordToken token);

    GetForgetPasswordResponse passwordReset(String email);

    void saveForgetPasswordToken(ForgetPasswordToken token);
    String generateToken();
    LocalDateTime expireTimeRange();

    ForgetPasswordToken getByToken(String token);
    void sendMail(String to,String object,String emailLink) throws MessagingException, UnsupportedEncodingException;
}
