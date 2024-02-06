package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Requests.CandidateRequest;
import com.devsling.fr.dto.Requests.EmployerCreateRequest;
import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.CandidateCreateResponse;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.JwtUtils;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.helper.Helper;
import com.devsling.fr.tools.Constants;
import com.devsling.fr.tools.RoleName;
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
    private final WebClient employerWebClient;


    @Override
    public Mono<RegisterResponse> signup(SignUpFormRequest signUpFormRequest) {
        RegisterResponse validationResponse = helper.validateSignUpFormRequest(signUpFormRequest);
        if ("Validation successful".equals(validationResponse.getMessage())) {
            if (signUpFormRequest.getRole_Name() != null) {
                AppUser appUserBd = AppUser.builder()
                        .username(signUpFormRequest.getUsername())
                        .email(signUpFormRequest.getEmail())
                        .password(bCryptPasswordEncoder.encode(signUpFormRequest.getPassword()))
                        .gender(signUpFormRequest.getGender())
                        .appRoles(Collections.singletonList(AppRole.builder()
                                .role(signUpFormRequest.getRole_Name())
                                .build()))
                        .build();

                // Check if the user has the role of a candidate
                if (appUserBd.getAppRoles().get(0).getRole().equals(RoleName.CANDIDATE.name())) {

                    // Construct the CandidateRequest object
                    CandidateRequest candidateRequest = CandidateRequest.builder()
                            .lastName(appUserBd.getUsername())
                            .email(appUserBd.getEmail())
                            .build();
                   // Save user authentication data in the database
                    userRepository.save(appUserBd);
                    // Make a POST request to the Candidate microservice to create the candidate
                   return candidateWebClient.post()
                           .uri("/candidates")
                           .bodyValue(candidateRequest) // Fournir les données du candidat
                           .retrieve()
                           .bodyToMono(CandidateCreateResponse.class)
                           .flatMap(createdCandidate -> {
                               // Si le candidat est créé avec succès, retourner une réponse de succès
                               return Mono.just(RegisterResponse.builder()
                                       .message("User registered successfully")
                                       .build());
                           })
                           .switchIfEmpty(Mono.just(RegisterResponse.builder()
                                   .message("Error creating candidate")
                                   .build()));
                }
                if (appUserBd.getAppRoles().get(0).getRole().equals(RoleName.EMPLOYER.name())) {

                    // Construct the Employer object
                    EmployerCreateRequest employerCreateRequest = EmployerCreateRequest.builder()
                            .lastName(appUserBd.getUsername())
                            .email(appUserBd.getEmail())
                            .build();
                   // Save user authentication data in the database
                    userRepository.save(appUserBd);
                    // Make a POST request to the Employer microservice to create the employer
                   return employerWebClient.post()
                           .uri("/employer")
                           .bodyValue(employerCreateRequest) // Fournir les données du employer
                           .retrieve()
                           .bodyToMono(CandidateCreateResponse.class)
                           .flatMap(createdCandidate -> {
                               // Si le candidat est créé avec succès, retourner une réponse de succès
                               return Mono.just(RegisterResponse.builder()
                                       .message("User registered successfully")
                                       .build());
                           })
                           .switchIfEmpty(Mono.just(RegisterResponse.builder()
                                   .message("Error creating candidate")
                                   .build()));
                } else {
                    // If the user is not a candidate, return response indicating they cannot be registered as a candidate
                    return Mono.just(RegisterResponse.builder()
                            .message("User cannot be registered as a candidate")
                            .build());
                }
            } else {
                // If the role is not specified, return a response indicating that the role should be specified
                return Mono.just(RegisterResponse.builder()
                        .message("Role should be specified")
                        .build());
            }
        } else {
            // If any validation error occurs, return error response
            return Mono.just(RegisterResponse.builder()
                    .message("Error registering user: " + validationResponse.getMessage())
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
