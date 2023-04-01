package com.srecko.reddit.filters;

import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The type Jwt authentication filter.
 *
 * @author Srecko Nikolic
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final UserService userService;

  private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

  /**
   * Instantiates a new Jwt authentication filter.
   *
   * @param jwtUtils    the jwt utils
   * @param userService the user service
   */
  @Autowired
  public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userService) {
    this.jwtUtils = jwtUtils;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String servletPath = request.getServletPath();
    logger.info("Filtering request with servlet path: {}", servletPath);
    if (servletPath.equals("/api/auth/register") || servletPath.equals(
        "/api/auth/registrationConfirm")
        || servletPath.equals("/api/auth/authenticate") || servletPath.contains(
        "/api/auth/token/refresh")
        || servletPath.contains("/api/search") || servletPath.endsWith("/api")) {
      filterChain.doFilter(request, response);
    } else {
      String authHeader = request.getHeader("AUTHORIZATION");
      if (authHeader == null || !authHeader.startsWith("Bearer")) {
        filterChain.doFilter(request, response);
      }
      String jwtToken = authHeader.substring(7);
      String username = jwtUtils.extractSubject(jwtToken);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        User userInstance = userService.getUserByUsername(username);
        UserMediator user = new UserMediator(userInstance);
        if (jwtUtils.isTokenValid(jwtToken, user.getUsername())) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              user, null, user.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    }
  }
}
