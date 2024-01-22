package com.devsling.fr.security;

import com.devsling.fr.entities.AppUser;
import com.devsling.fr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userService.findUserByUsername(username);
        if (appUser == null) throw new UsernameNotFoundException("User not exist with name :" + username);
        String pass = appUser.getPassword();

        List<GrantedAuthority> auths = new ArrayList<>();
        appUser.getAppRoles().forEach(role -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole().toString());
            auths.add(authority);
        });

        return new org.springframework.security.core.userdetails.User(username, pass, auths);
    }
}
