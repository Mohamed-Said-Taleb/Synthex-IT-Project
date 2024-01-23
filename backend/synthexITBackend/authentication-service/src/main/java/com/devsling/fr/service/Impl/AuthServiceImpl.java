package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.LoginForm;
import com.devsling.fr.dto.SignupForm;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.JwtUtils;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.service.helper.UserServiceHelper;
import com.devsling.fr.tools.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserServiceHelper userServiceHelper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserServiceHelper userServiceHelper, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userServiceHelper = userServiceHelper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<?> signup(SignupForm signUpForm) {
        if (userRepository.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ErrorModel("Username is used "), HttpStatus.BAD_REQUEST);
        }
        if (signUpForm.getUsername().isEmpty()) {
            return new ResponseEntity<>(new ErrorModel("Username should not be empty"), HttpStatus.BAD_REQUEST);
        }
        if (signUpForm.getPassword().isEmpty()) {
            return new ResponseEntity<>(new ErrorModel("Password should not be empty"), HttpStatus.BAD_REQUEST);
        }
        if (signUpForm.getPassword().length() < 5) {
            return new ResponseEntity<>(new ErrorModel("Short PassWord"), HttpStatus.BAD_REQUEST);

        }
        if (signUpForm.getEmail().isEmpty()) {
            return new ResponseEntity<>(new ErrorModel("Email should not be empty"), HttpStatus.BAD_REQUEST);
        }
        if (!(userServiceHelper.isValidEmailAddress(signUpForm.getEmail()))) {
            return new ResponseEntity<>(new ErrorModel("Invalid email"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>(new ErrorModel("email is used"), HttpStatus.BAD_REQUEST);
        }
        AppUser appUserBd = AppUser.builder()
                .username(signUpForm.getUsername())
                .email(signUpForm.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpForm.getPassword()))
                .gender(signUpForm.getGender())
                .enabled(signUpForm.getEnabled())
                .appRoles(Collections.singletonList(roleRepository.findByRole(signUpForm.getRole_Name())))
                .build();

        userRepository.save(appUserBd);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Override
    public String getToken(LoginForm loginForm) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
            if (authenticate.isAuthenticated()) {
                return jwtUtils.generateToken(loginForm.getUsername());
            } else {
                throw new RuntimeException("Invalid access");
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            jwtUtils.validateToken(token);
            return "Token valid";
        } catch (Exception e) {
            return "Invalid token: " + e.getMessage();
        }
    }
}
