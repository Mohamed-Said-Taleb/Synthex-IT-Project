package com.devsling.fr.controller;

import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetForgetPasswordResponse;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.ProfileResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.dto.Responses.VerificationResponse;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.tools.Constants;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponse>> register(@Valid @RequestBody SignUpFormRequest signUpFormRequest) {
        return authService.signup(signUpFormRequest)
                .map(signupResponse -> {
                    if (!(signupResponse.getMessage().equals(Constants.USER_REGISTERED_SUCCESSFULLY))){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signupResponse);
                    }
                 return ResponseEntity.status(HttpStatus.OK).body(signupResponse);
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<GetTokenResponse>> login(@Valid @RequestBody LoginFormRequest loginFormRequest) {
        return authService.getToken(loginFormRequest)
                .map(tokenResponse -> {
                    if (tokenResponse.getMessage().equals(Constants.INVALID_USERNAME_OR_PASSWORD)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenResponse);
                    }
                    if (tokenResponse.getMessage().equals(Constants.ENABLED_ACCOUNT)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenResponse);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
                });

    }

    @PostMapping("/validate-token")
    public Mono<ResponseEntity<GetTokenValidationResponse>> validateToken(@RequestParam("token") String token) {
        return authService.validateToken(token)
                .map(tokenValidationResponse ->{
                    if (tokenValidationResponse.getMessage().equals(Constants.INVALID_TOKEN)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenValidationResponse);
                    }
                   return ResponseEntity.status(HttpStatus.OK).body(tokenValidationResponse);
                } );
    }

    @PostMapping("/password-request")
    public  Mono<ResponseEntity<GetForgetPasswordResponse>> passwordRequestWithEmail(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        return authService.passwordResetMail(email)
                .map(tokenValidationResponse -> {
                    if (tokenValidationResponse.getMessage().equals(Constants.WRONG_PASSWORD)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenValidationResponse);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(tokenValidationResponse);
                });
    }

    @PostMapping("/reset-password")
    public Mono<ResponseEntity<GetTokenValidationResponse>> resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmationPassword") String confirmationPassword) {
        return authService.validatePasswordReset(token, password, confirmationPassword)
                .map(tokenValidationResponse ->{
                    if (tokenValidationResponse.getMessage().equals(Constants.WRONG_PASSWORD)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenValidationResponse);
                    }
                    if (tokenValidationResponse.getMessage().equals(Constants.INVALID_PASSWORD_RESET_TOKEN)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenValidationResponse);
                    } if (tokenValidationResponse.getMessage().equals(Constants.PASSWORD_RESET_NOT_MATCH)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenValidationResponse);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(tokenValidationResponse);
                } );
    }
    @GetMapping("/activate-account")
    public Mono<ResponseEntity<VerificationResponse>> activateAccountWithEmail(@RequestParam("token") String token) {
        return authService.verifyAccountWithEmail(token)
                .map(verificationResponse ->{
                    if (verificationResponse.getMessage().equals(Constants.FAILED_VERIFICATION)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(verificationResponse);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(verificationResponse);
                } );
    }

    @GetMapping("/profile")
    public Mono<ResponseEntity<ProfileResponse>> getProfile(@RequestParam("email") String email) {
        return authService.getProfile(email)
                .map(verificationResponse -> ResponseEntity.status(HttpStatus.OK).body(verificationResponse));
    }
}
