package com.devsling.fr.service;

import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.entities.ForgetPasswordToken;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import reactor.core.publisher.Mono;

public interface EmailSenderService {

    Mono<GetTokenValidationResponse> validatePasswordReset(String token, String password, String confirmationPassword);

    Mono<GetForgetPasswordResponse> passwordResetMail(String email);


    String generateToken();

    ForgetPasswordToken getByToken(String token);
    void sendMail(String username,String to,String object,String emailLink,String emailTemplate) throws MessagingException, UnsupportedEncodingException;
}
