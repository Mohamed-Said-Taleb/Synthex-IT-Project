package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.JwtUtils;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.helper.Helper;
import com.devsling.fr.tools.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Helper helper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final WebClient candidateWebClient;


    @Override
    public Mono<RegisterResponse> signup(SignUpFormRequest signUpFormRequest) {
      helper.validateSignUpFormRequest(signUpFormRequest);
        try {
            AppUser appUserBd = AppUser.builder()
                    .username(signUpFormRequest.getUsername())
                    .email(signUpFormRequest.getEmail())
                    .password(bCryptPasswordEncoder.encode(signUpFormRequest.getPassword()))
                    .gender(signUpFormRequest.getGender())
                    .appRoles(Collections.singletonList(roleRepository.findByRole(signUpFormRequest.getRole_Name())))
                    .build();

            userRepository.save(appUserBd);

          /**  candidateWebClient.post()
                    .uri("/candidate/create")
                    .body(appUserBd)
                    .retrieve()
                    .bodyToMono(ValidateTokenResponse.class);**/



            return Mono.just(RegisterResponse.builder()
                    .message("User registered successfully")
                    .build());
        } catch (Exception e) {
            return Mono.just(RegisterResponse.builder()
                    .message("Error registering user: " + e.getMessage())
                    .build());
        }
    }



    @Override
    public Mono<GetTokenResponse> getToken(LoginFormRequest loginFormRequest) {
        RegisterResponse validationResponse = helper.validateLoginFormRequest(loginFormRequest);

        if (!"Validation successful".equals(validationResponse.getMessage())) {
            return Mono.just(GetTokenResponse.builder()
                    .message(validationResponse.getMessage())
                    .build());
        }

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginFormRequest.getUsername(), loginFormRequest.getPassword())
            );

            if (authenticate.isAuthenticated()) {
                String token = jwtUtils.generateToken(loginFormRequest.getUsername(), authenticate);
                return Mono.just(GetTokenResponse.builder()
                        .token(token)
                        .message("Authentication successful")
                        .build());
            } else {
                return Mono.just(GetTokenResponse.builder()
                        .message("Invalid access")
                        .build());
            }
        } catch (AuthenticationException e) {
            return Mono.just(GetTokenResponse.builder()
                    .message("Invalid username or password")
                    .build());
        }
    }
    @Override
    public Mono<GetTokenValidationResponse> validateToken(String token) {
        boolean isTokenValid = jwtUtils.validateToken(token);

        if (isTokenValid) {
            return Mono.just(GetTokenValidationResponse.builder()
                    .message(Constants.VALID_TOKEN)
                    .build());
        } else {
            return Mono.just(GetTokenValidationResponse.builder()
                    .message(Constants.INVALID_TOKEN)
                    .build());
        }
    }
}
