package com.devsling.fr.service;

import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.RegisterResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    RegisterResponse signup(SignUpFormRequest signUpFormRequest);

    GetTokenResponse getToken(LoginFormRequest loginFormRequest);
    GetTokenValidationResponse validateToken(String token);
}
