package com.devsling.fr.controller;

import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignupForm;
import com.devsling.fr.security.MyUserDetailsService;
import com.devsling.fr.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupForm signUpForm) {
        return authService.signup(signUpForm);
    }
    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody LoginForm loginForm) {
        try {
            String token = authService.getToken(loginForm);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }}

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        try {
            String result = authService.validateToken(token);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
