package com.devsling.fr.controller;

import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignupForm;
import com.devsling.fr.security.MyUserDetailsService;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.tools.TokenValidationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;




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
    public Mono<TokenValidationResponse> validateToken(@RequestParam("token") String token) {
        return Mono.fromCallable(() -> {
                    TokenValidationResponse result = authService.validateToken(token);
                    return result;
                })
                .onErrorResume(Exception.class, e -> {
                    return Mono.just(new TokenValidationResponse("Invalid token: " + e.getMessage()));
                });

}}
