package com.srecko.reddit.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class JwtUtil {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String getAccessToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .withIssuer("auth0")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String getRefreshToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusWeeks(4)))
                .withIssuer("auth0")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }
}
