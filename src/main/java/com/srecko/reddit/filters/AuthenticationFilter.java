package com.srecko.reddit.filters;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.jwt.JwtConfig;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Authentication filter.
 *
 * @author Srecko Nikolic
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtConfig jwtConfig;

  private final AuthenticationManager authenticationManager;

  private final RefreshTokenService refreshTokenService;

  private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);

  /**
   * Instantiates a new Authentication filter.
   *
   * @param jwtConfig             the jwt config
   * @param authenticationManager the authentication manager
   * @param refreshTokenService   the refresh token service
   */
  @Autowired
  public AuthenticationFilter(JwtConfig jwtConfig, AuthenticationManager authenticationManager,
      RefreshTokenService refreshTokenService) {
    this.jwtConfig = jwtConfig;
    this.authenticationManager = authenticationManager;
    this.refreshTokenService = refreshTokenService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    logger.info("Attempting authentication");
    String usernameJson = "";
    String passwordJson = "";
    try {
      JsonNode jsonNode = new ObjectMapper().readTree(request.getInputStream());
      usernameJson = jsonNode.get("username").asText();
      passwordJson = jsonNode.get("password").asText();
    } catch (Exception e) {
      e.printStackTrace();
    }
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(usernameJson, passwordJson);
    logger.debug("Created authentication token. Authenticating {}", usernameJson);
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException {
    UserMediator userMediator = (UserMediator) authentication.getPrincipal();
    JwtUtils jwtUtils = new JwtUtils(jwtConfig);
    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", jwtUtils.getAccessToken(userMediator.getUsername()));
    String refreshToken = jwtUtils.getRefreshToken(userMediator.getUsername());
    tokens.put("refreshToken", refreshToken);
    refreshTokenService.saveRefreshToken(refreshToken, userMediator.getUsername());
    response.setContentType(APPLICATION_JSON_VALUE);
    logger.info("Authenticated successfully: {}", userMediator.getUsername());
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
