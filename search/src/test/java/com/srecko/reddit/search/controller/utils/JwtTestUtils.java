package com.srecko.reddit.search.controller.utils;

/*
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtTestUtils {

  public static String getJwt() {
    return generateJwt("janedoe@example.com", Instant.now().plus(1, ChronoUnit.DAYS));
  }

  public static String getJwt(String subject, Instant expiration) {
    return generateJwt(subject, expiration);
  }

  private static String generateJwt(String subject, Instant expiration) {
    return Jwts
        .builder()
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(Date.from(expiration))
        .setIssuer("auth0")
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(System.getenv("SECRET_KEY"))),
            SignatureAlgorithm.HS256)
        .compact();
  }
}*/
