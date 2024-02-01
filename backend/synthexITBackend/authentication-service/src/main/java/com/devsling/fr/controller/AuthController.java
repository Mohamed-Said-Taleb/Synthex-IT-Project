package com.devsling.fr.controller;

import com.devsling.fr.dto.GetForgetPasswordResponse;
import com.devsling.fr.dto.GetTokenResponse;
import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignUpForm;
import com.devsling.fr.dto.GetTokenValidationResponse;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.ForgetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpForm signUpForm) {
        return authService.signup(signUpForm);
    }
    @PostMapping("/login")
    public Mono<GetTokenResponse> getToken(@RequestBody LoginForm loginForm) {
        return Mono.fromCallable(() -> {
                   return authService.getToken(loginForm);
                })
                .onErrorResume(Exception.class, e -> Mono.just(new GetTokenResponse("Invalid access",null,null)));
    }

    @PostMapping("/validate-token")
    public Mono<GetTokenValidationResponse> validateToken(@RequestParam("token") String token) {
        return Mono.fromCallable(() -> {
                    GetTokenValidationResponse result = authService.validateToken(token);
                    return result;
                })
                .onErrorResume(Exception.class, e -> Mono.just(new GetTokenValidationResponse("Invalid token: ")));
    }
    @PostMapping("/password-request")
    public Mono<GetForgetPasswordResponse> passwordRequest(@RequestParam("email") String email) {
        return Mono.fromCallable(() -> forgetPasswordService.passwordReset(email))
                .onErrorResume(Exception.class, e -> Mono.just(new GetForgetPasswordResponse(e.getMessage(),null)));
    }
    @GetMapping("/reset-password")
    public Mono<GetTokenValidationResponse> resetPassword(@RequestParam("token") String token,
                                                          @RequestParam("password") String password,
                                                          @RequestParam("confirmationPassword") String confirmationPassword) {
        return Mono.fromCallable(() -> forgetPasswordService.resetPasswordAndValidateToken(token,password,confirmationPassword))
                .onErrorResume(Exception.class, e -> Mono.just(new GetTokenValidationResponse("Invalid token: ")));
    }
}
