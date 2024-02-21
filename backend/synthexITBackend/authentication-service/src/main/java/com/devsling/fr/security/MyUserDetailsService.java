package com.devsling.fr.security;

import com.devsling.fr.entities.AppUser;
import com.devsling.fr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService, OAuth2User {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = userService.findUserByUsername(username);
        if (appUser.isEmpty()) throw new UsernameNotFoundException("User not exist with name :" + username);
        String pass = appUser.get().getPassword();

        List<GrantedAuthority> auths = new ArrayList<>();
        appUser.get().getAppRoles().forEach(role -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole().toString());
            auths.add(authority);
        });
        return new org.springframework.security.core.userdetails.User(username, pass, auths);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
