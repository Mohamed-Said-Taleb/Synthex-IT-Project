package com.devsling.fr.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devsling.fr.tools.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
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


    public  Map<String,String> generateToken(String userName,Authentication authResult) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName,authResult);
    }

    private Map<String,String> createToken(Map<String, Object> claims, String userName, Authentication authResult) {
        org.springframework.security.core.userdetails.User SpringUser = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        List<String> roles = new ArrayList<>();
        SpringUser.getAuthorities().forEach(au -> {
            roles.add(au.getAuthority());

        });

        String jwtAccessToken = JWT
                .create()
                .withSubject(SpringUser.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(Constants.Secret));
        String jwtRefreshToken = JWT
                .create()
                .withSubject(SpringUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +  2 * 60 * 60 * 1000))// use long duration
                .sign(Algorithm.HMAC256(Constants.Secret));

        Map<String,String> idToken=new HashMap<>();
        idToken.put("access-token",jwtAccessToken);
        idToken.put("refresh-token",jwtRefreshToken);
        return idToken;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
