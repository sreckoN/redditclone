package com.srecko.reddit.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.time.Instant;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private final String secretKey;
    private final Integer accessTokenExpiration;
    private final Integer refreshTokenExpiration;

    public JwtConfig() {
        this.secretKey = System.getenv("SECRET_KEY");
        this.accessTokenExpiration = Integer.parseInt(System.getenv("ACCESS_TOKEN_EXPIRATION_MINUTES"));
        this.refreshTokenExpiration = Integer.parseInt(System.getenv("REFRESH_TOKEN_EXPIRATION_DAYS"));
    }

    public Key getKeySecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Integer getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public Integer getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}