package com.devsling.fr.security;

import com.devsling.fr.entities.User;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not exist with name :" + username);
        String pass = user.getPassword();

        List<GrantedAuthority> auths = new ArrayList<>();
        user.getRoles().forEach(role -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole().toString());
            auths.add(authority);
        });

        return new org.springframework.security.core.userdetails.User(username, pass, auths);
    }
}
