package com.devsling.fr.controller;

import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.ForgetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ForgetPasswordService forgetPasswordService;

    @PostMapping("/register")
    public Mono<RegisterResponse> register(@Valid @RequestBody SignUpFormRequest signUpFormRequest) {
        return Mono.fromCallable(() -> authService.signup(signUpFormRequest))
                .onErrorResume(Exception.class, e -> Mono.just(new RegisterResponse(e.getMessage())));
    }

    @PostMapping("/login")
    public Mono<GetTokenResponse> login(@Valid @RequestBody LoginFormRequest loginFormRequest) {
        return Mono.fromCallable(() -> authService.getToken(loginFormRequest))
                .onErrorResume(Exception.class, e -> Mono.just(new GetTokenResponse(null,e.getMessage())));
    }

    @PostMapping("/validate-token")
    public Mono<GetTokenValidationResponse> validateToken(@RequestParam("token") String token) {
        return Mono.fromCallable(() -> authService.validateToken(token))
                .onErrorResume(Exception.class, e -> Mono.just(new GetTokenValidationResponse(e.getMessage())));
    }

    @PostMapping("/password-request")
    public Mono<GetForgetPasswordResponse> passwordRequest(@RequestParam("email") String email) {
        return Mono.fromCallable(() -> forgetPasswordService.passwordResetMail(email))
                .onErrorResume(Exception.class, e -> Mono.just(new GetForgetPasswordResponse(e.getMessage(), null)));
    }

    @PostMapping("/reset-password")
    public Mono<GetTokenValidationResponse> resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmationPassword") String confirmationPassword) {
        return Mono.fromCallable(() -> forgetPasswordService.validatePasswordReset(token, password, confirmationPassword))
                .onErrorResume(Exception.class, e -> Mono.just(new GetTokenValidationResponse(e.getMessage())));
    }
}
