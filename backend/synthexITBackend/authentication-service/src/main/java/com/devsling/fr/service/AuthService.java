package com.devsling.fr.service;

import com.devsling.fr.dto.GetTokenResponse;
import com.devsling.fr.dto.GetTokenValidationResponse;
import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignUpForm;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> signup(SignUpForm signUpForm);

    GetTokenResponse getToken(LoginForm loginForm);
    GetTokenValidationResponse validateToken(String token);
}
