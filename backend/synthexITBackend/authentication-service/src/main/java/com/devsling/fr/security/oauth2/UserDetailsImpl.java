package com.devsling.fr.security.oauth2;


import com.devsling.fr.entities.AppUser;
import com.devsling.fr.tools.RoleName;
import jakarta.persistence.Column;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class UserDetailsImpl implements UserDetails, OAuth2User {
    private final Long id;
    private final String email;
    private final String name;
    private final String stripeCustomerId;
    @JsonIgnore
    private String password;
    private final String imageUrl;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;


    public UserDetailsImpl(Long id, String email, String name,
                           String stripeCustomerId
            , String password, Collection<? extends GrantedAuthority> authorities, String imageUrl) {

        this.id = id;
        this.email = email;
        this.name = name;
        this.stripeCustomerId = stripeCustomerId;
        this.password = password;

        this.imageUrl = imageUrl;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return null;
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
    public String getPassword() {
        return null;
    }
    @Override
    public String getUsername() {
        return null;
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    @Override
    public boolean isEnabled() {
        return false;
    }
    private void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public static UserDetailsImpl build(AppUser user, Map<String, Object> attributes) {
        UserDetailsImpl userPrincipal = UserDetailsImpl.build(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }
    public static UserDetailsImpl build(AppUser user) {
        if (user.getAppRoles() != null) {
            if (user.getAppRoles().contains(RoleName.ADMIN.name())) {
                return buildAdmin(user);
            } else if (user.getAppRoles().contains(RoleName.CANDIDATE.name())) {
                return buildCandidate(user);
            } else if (user.getAppRoles().contains(RoleName.EMPLOYER.name())) {
                return buildEmployer(user);
            }
        }
        return buildCandidate(user);
    }
    public static UserDetailsImpl buildCandidate(AppUser user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("CANDIDATE"));
        return new UserDetailsImpl(
                user.getIdUser(),
                user.getEmail(),
                user.getUsername(),
                user.getStripeCustomerId(),
                user.getPassword(),
                authorities,
                user.getImageUrl()
        );
    }
    public static UserDetailsImpl buildEmployer(AppUser user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("EMPLOYER"));
        return new UserDetailsImpl(
                user.getIdUser(),
                user.getEmail(),
                user.getUsername(),
                user.getStripeCustomerId(),
                user.getPassword(),
                authorities,
                user.getImageUrl()
        );
    }
    public static UserDetailsImpl buildAdmin(AppUser user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        return new UserDetailsImpl(
                user.getIdUser(),
                user.getEmail(),
                user.getUsername(),
                user.getStripeCustomerId(),
                user.getPassword(),
                authorities,
                user.getImageUrl()
        );
    }
}
