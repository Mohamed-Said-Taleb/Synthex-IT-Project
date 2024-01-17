package com.devsling.fr.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {


    private final UserDetailsService userDetailsService;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;



}
