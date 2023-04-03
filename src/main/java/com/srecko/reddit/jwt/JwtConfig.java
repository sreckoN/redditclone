package com.srecko.reddit.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jwt config.
 *
 * @author Srecko Nikolic
 */
public class JwtConfig {

  private final String secretKey;
  private final Integer accessTokenExpiration;
  private final Integer refreshTokenExpiration;

  /**
   * Instantiates a new Jwt config.
   */
  public JwtConfig() {
    this.secretKey = System.getenv("SECRET_KEY");
    this.accessTokenExpiration = Integer.parseInt(System.getenv("ACCESS_TOKEN_EXPIRATION_MINUTES"));
    this.refreshTokenExpiration = Integer.parseInt(System.getenv("REFRESH_TOKEN_EXPIRATION_DAYS"));
  }

  /**
   * Gets key secret key.
   *
   * @return the key secret key
   */
  public Key getKeySecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Gets secret key.
   *
   * @return the secret key
   */
  public String getSecretKey() {
    return secretKey;
  }

  /**
   * Gets access token expiration.
   *
   * @return the access token expiration
   */
  public Integer getAccessTokenExpiration() {
    return accessTokenExpiration;
  }

  /**
   * Gets refresh token expiration.
   *
   * @return the refresh token expiration
   */
  public Integer getRefreshTokenExpiration() {
    return refreshTokenExpiration;
  }
}