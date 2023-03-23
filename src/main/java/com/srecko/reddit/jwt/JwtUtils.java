package com.srecko.reddit.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Jwt utils.
 *
 * @author Srecko Nikolic
 */
@Component
public class JwtUtils {

  private final JwtConfig jwtConfig;

  /**
   * Instantiates a new Jwt utils.
   *
   * @param jwtConfig the jwt config
   */
  @Autowired
  public JwtUtils(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  /**
   * Gets access token.
   *
   * @param username the username
   * @return the access token
   */
  public String getAccessToken(String username) {
    Integer accessTokenExpiration = jwtConfig.getAccessTokenExpiration();
    Instant expirationDate = Instant.now().plus(accessTokenExpiration, ChronoUnit.HOURS);
    return generateToken(username, expirationDate);
  }

  /**
   * Gets refresh token.
   *
   * @param username the username
   * @return the refresh token
   */
  public String getRefreshToken(String username) {
    Integer refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
    Instant expirationDate = Instant.now().plus(refreshTokenExpiration, ChronoUnit.DAYS);
    return generateToken(username, expirationDate);
  }

  private String generateToken(String username, Instant expirationDate) {
    return Jwts
        .builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(Date.from(expirationDate))
        .setIssuer("auth0")
        .signWith(jwtConfig.getKeySecretKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Extract subject string.
   *
   * @param token the token
   * @return the string
   */
  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extract expiration date.
   *
   * @param token the token
   * @return the date
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Has claim boolean.
   *
   * @param token     the token
   * @param claimName the claim name
   * @return the boolean
   */
  public boolean hasClaim(String token, String claimName) {
    final Claims claims = extractAllClaims(token);
    return claims.get(claimName) != null;
  }

  /**
   * Extract claim t.
   *
   * @param <T>            the type parameter
   * @param token          the token
   * @param claimsResolver the claims resolver
   * @return the t
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(jwtConfig.getSecretKey()).build()
        .parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Is token valid boolean.
   *
   * @param token the token
   * @param email the email
   * @return the boolean
   */
  public Boolean isTokenValid(String token, String email) {
    final String username = extractSubject(token);
    return (username.equals(email) && !isTokenExpired(token));
  }
}
