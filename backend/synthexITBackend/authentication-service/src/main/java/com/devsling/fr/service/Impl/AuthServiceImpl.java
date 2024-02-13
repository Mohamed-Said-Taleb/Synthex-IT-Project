package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Requests.CandidateRequest;
import com.devsling.fr.dto.Requests.EmployerCreateRequest;
import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.dto.Responses.VerificationResponse;
import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.JwtUtils;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.helper.Helper;
import com.devsling.fr.service.out.CandidateApiClient;
import com.devsling.fr.service.out.EmployerApiClient;
import com.devsling.fr.tools.Constants;
import com.devsling.fr.tools.RoleName;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final Helper helper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CandidateApiClient candidateApiClient;
    private final EmployerApiClient employerApiClient;
    private final EmailSenderImpl emailSender;

    public static final String  VERIFICATION_EMAIL_TEMPLATE = "verification-email-template";



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
                        .enabled(false)
                        .verificationCode(RandomString.make(64))
                        .gender(signUpFormRequest.getGender())
                        .appRoles(Collections.singletonList(AppRole.builder()
                                .role(signUpFormRequest.getRole_Name())
                                .build()))
                        .build();

                if (appUserBd.getAppRoles().get(0).getRole().equals(RoleName.CANDIDATE.name())) {
                    CandidateRequest candidateRequest = CandidateRequest.builder()
                            .lastName(appUserBd.getUsername())
                            .email(appUserBd.getEmail())
                            .firstName(signUpFormRequest.getFirstName())
                            .resumeUrl(signUpFormRequest.getResumeUrl())
                            .professionalExperiences(signUpFormRequest.getProfessionalExperiences())
                            .build();
                    return candidateApiClient.saveCandidate(candidateRequest)
                            .flatMap(candidateCreateResponse -> {
                                userRepository.save(appUserBd);
                                emailSender.sendMail(appUserBd.getUsername(),appUserBd.getEmail(),"Activate your SynthexIT account","link",VERIFICATION_EMAIL_TEMPLATE);

                                return Mono.just(RegisterResponse.builder()
                                        .message(Constants.REGISTRATION_CANDIDATE_MESSAGE)
                                        .build());
                            })
                            .onErrorResume(throwable -> Mono.just(RegisterResponse.builder()
                                    .message("Error creating candidate: " + throwable.getMessage())
                                    .build()));
                }
                if (appUserBd.getAppRoles().get(0).getRole().equals(RoleName.EMPLOYER.name())) {

                    EmployerCreateRequest employerCreateRequest = EmployerCreateRequest.builder()
                            .lastName(appUserBd.getUsername())
                            .email(appUserBd.getEmail())
                            .build();
                    userRepository.save(appUserBd);
                    return employerApiClient.saveEmployer(employerCreateRequest)
                            .flatMap(candidateCreateResponse -> {
                                userRepository.save(appUserBd);
                                emailSender.sendMail(appUserBd.getUsername(),appUserBd.getEmail(),"Activate your SynthexIT account","link",VERIFICATION_EMAIL_TEMPLATE);
                                return Mono.just(RegisterResponse.builder()
                                        .message(Constants.REGISTRATION_EMPLOYER_MESSAGE)
                                        .build());
                            })
                            .onErrorResume(throwable -> Mono.just(RegisterResponse.builder()
                                    .message("Error creating candidate: " + throwable.getMessage())
                                    .build()));
                } else {
                    return Mono.just(RegisterResponse.builder()
                            .message("User cannot be registered as a candidate")
                            .build());
                }
            } else {
                return Mono.just(RegisterResponse.builder()
                        .message("Role should be specified")
                        .build());
            }
        } else {
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
            return Mono.just(GetTokenValidationResponse
                    .builder()
                    .message(Constants.VALID_TOKEN)
                    .build());
        } else {
            return Mono.just(GetTokenValidationResponse
                    .builder()
                    .message(Constants.INVALID_TOKEN)
                    .build());
        }
    }

    @Override
    public Mono<VerificationResponse> verifyAccountWithEmail(String token) {
        Optional<AppUser> user =userRepository.findByVerificationCode(token);
        if (user.isPresent()) {
            user.get().setEnabled(true);
            return Mono.just(VerificationResponse
                    .builder()
                    .message(Constants.SUCCESS_VERIFICATION)
                    .build());
        } else {
            return Mono.just(VerificationResponse
                    .builder()
                    .message(Constants.FAILED_VERIFICATION)
                    .build());
        }
    }
}
