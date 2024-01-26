package com.devsling.fr.service;

import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignupForm;
import com.devsling.fr.security.MyUserDetailsService;
import com.devsling.fr.tools.TokenValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    ResponseEntity<?> signup(SignupForm signUpForm);

     String getToken(LoginForm loginForm);
    TokenValidationResponse validateToken(String token);
}
