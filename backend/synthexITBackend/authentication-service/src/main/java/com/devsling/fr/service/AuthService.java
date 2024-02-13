package com.devsling.fr.service;

import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.dto.Responses.VerificationResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<RegisterResponse> signup(SignUpFormRequest signUpFormRequest);

    Mono<GetTokenResponse> getToken(LoginFormRequest loginFormRequest);
    Mono<GetTokenValidationResponse> validateToken(String token);

    Mono<VerificationResponse> verifyAccountWithEmail(String token);

}
