package com.srecko.reddit.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String getAccessToken(User user, String url) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(url)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String getRefreshToken(User user, String url) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusWeeks(4)))
                .withIssuer(url)
                .sign(algorithm);
    }
}
