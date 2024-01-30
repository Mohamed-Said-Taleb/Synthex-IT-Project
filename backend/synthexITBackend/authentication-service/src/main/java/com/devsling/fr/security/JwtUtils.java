package com.devsling.fr.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public boolean validateToken(final String token) {
        try {
             Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);

            // Token is valid
            return true;
        } catch (Exception e) {
            // Token validation failed
            return false;
        }
    }


    public String generateToken(String userName,Authentication authResult) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName,authResult);
    }

    private String createToken(Map<String, Object> claims, String userName, Authentication authResult) {
        org.springframework.security.core.userdetails.User SpringUser = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        List<String> roles = new ArrayList<>();
        SpringUser.getAuthorities().forEach(au -> {
            roles.add(au.getAuthority());

        });
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userName)
                    .claim("roles", roles)  // Add roles as a custom claim
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
