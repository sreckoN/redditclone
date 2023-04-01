package com.srecko.reddit.config;

import com.srecko.reddit.controller.VoteController;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * The type App logout handler.
 *
 * @author Srecko Nikolic
 */
@Service
public class AppLogoutHandler implements LogoutHandler {

  private final RefreshTokenService refreshTokenService;

  private static final Logger logger = LogManager.getLogger(AppLogoutHandler.class);

  /**
   * Instantiates a new App logout handler.
   *
   * @param refreshTokenService the refresh token service
   */
  @Autowired
  public AppLogoutHandler(RefreshTokenService refreshTokenService) {
    this.refreshTokenService = refreshTokenService;
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    User principal = (User) authentication.getPrincipal();
    refreshTokenService.deleteRefreshToken(principal);
    SecurityContextHolder.clearContext();
    logger.info("Logged out user: {}", principal.getEmail());
  }
}
