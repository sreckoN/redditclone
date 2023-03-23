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

@Component
public class JwtUtils {

  private final JwtConfig jwtConfig;

  @Autowired
  public JwtUtils(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String getAccessToken(String username) {
    Integer accessTokenExpiration = jwtConfig.getAccessTokenExpiration();
    Instant expirationDate = Instant.now().plus(accessTokenExpiration, ChronoUnit.HOURS);
    return generateToken(username, expirationDate);
  }

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

  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public boolean hasClaim(String token, String claimName) {
    final Claims claims = extractAllClaims(token);
    return claims.get(claimName) != null;
  }

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

  public Boolean isTokenValid(String token, String email) {
    final String username = extractSubject(token);
    return (username.equals(email) && !isTokenExpired(token));
  }
}
