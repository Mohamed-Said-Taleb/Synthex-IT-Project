package com.devsling.fr.service;

import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.dto.Responses.VerificationResponse;
import jakarta.mail.MessagingException;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    Mono<RegisterResponse> signup(SignUpFormRequest signUpFormRequest);

    Mono<GetTokenResponse> getToken(LoginFormRequest loginFormRequest);
    Mono<GetTokenValidationResponse> validateToken(String token);

    Mono<VerificationResponse> verifyAccountWithEmail(String token);
    Mono<GetTokenValidationResponse> validatePasswordReset(String token, String password, String confirmationPassword);

    Mono<GetForgetPasswordResponse> passwordResetMail(String email) throws MessagingException, UnsupportedEncodingException;

}
