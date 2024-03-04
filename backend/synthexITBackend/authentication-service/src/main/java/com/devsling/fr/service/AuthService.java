package com.devsling.fr.service;

import com.devsling.fr.dto.requests.LoginFormRequest;
import com.devsling.fr.dto.requests.SignUpFormRequest;
import com.devsling.fr.dto.responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.responses.GetTokenResponse;
import com.devsling.fr.dto.responses.GetTokenValidationResponse;
import com.devsling.fr.dto.responses.ProfileResponse;
import com.devsling.fr.dto.responses.RegisterResponse;
import com.devsling.fr.dto.responses.VerificationResponse;
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
    Mono<ProfileResponse> getProfile(String email);
}
