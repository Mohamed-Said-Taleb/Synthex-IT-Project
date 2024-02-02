package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.Responses.GetTokenResponse;
import com.devsling.fr.dto.Responses.GetTokenValidationResponse;
import com.devsling.fr.dto.Requests.LoginFormRequest;
import com.devsling.fr.dto.Requests.SignUpFormRequest;
import com.devsling.fr.dto.Responses.RegisterResponse;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.JwtUtils;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.helper.Helper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final Helper helper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, Helper helper, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.helper = helper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public RegisterResponse signup(SignUpFormRequest signUpFormRequest) {
        RegisterResponse validationResponse = helper.validateSignUpFormRequest(signUpFormRequest);
        if (!validationResponse.getMessage().equals("Validation successful")) {
            return validationResponse;
        }
        try {
            AppUser appUserBd = AppUser.builder()
                    .username(signUpFormRequest.getUsername())
                    .email(signUpFormRequest.getEmail())
                    .password(bCryptPasswordEncoder.encode(signUpFormRequest.getPassword()))
                    .gender(signUpFormRequest.getGender())
                    .appRoles(Collections.singletonList(roleRepository.findByRole(signUpFormRequest.getRole_Name())))
                    .build();

            userRepository.save(appUserBd);

            return RegisterResponse.builder()
                    .message("User registered successfully")
                    .build();
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Error registering user: " + e.getMessage())
                    .build();
        }
    }



    public GetTokenResponse getToken(LoginFormRequest loginFormRequest) {
        RegisterResponse validationResponse = helper.validateLoginFormRequest(loginFormRequest);
        if (!"Validation successful".equals(validationResponse.getMessage())) {
            return GetTokenResponse.builder()
                    .message(validationResponse.getMessage())
                    .build();
        }

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginFormRequest.getUsername(), loginFormRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                String token = jwtUtils.generateToken(loginFormRequest.getUsername(), authenticate);
                return GetTokenResponse.builder()
                        .token(token)
                        .build();
            } else {
                return GetTokenResponse.builder()
                        .message("Invalid access ")
                        .build();
            }
        } catch (AuthenticationException e) {
            return GetTokenResponse.builder()
                    .message("Invalid username or password")
                    .build();
        }
    }

    @Override
    public GetTokenValidationResponse validateToken(String token) {

            if ((jwtUtils.validateToken(token))){
            return GetTokenValidationResponse.builder()
                    .status("Valid token").build();
            }
            return GetTokenValidationResponse.builder()
                    .status("Invalid token").build();
    }
}
