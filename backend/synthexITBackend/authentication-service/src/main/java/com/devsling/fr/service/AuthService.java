package com.devsling.fr.service;

import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignupForm;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> signup(SignupForm signUpForm);

     String getToken(LoginForm loginForm);
    String validateToken( String token);
}
