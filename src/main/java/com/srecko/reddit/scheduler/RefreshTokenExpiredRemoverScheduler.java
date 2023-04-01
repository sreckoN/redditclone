package com.srecko.reddit.scheduler;

import com.srecko.reddit.service.RefreshTokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The type Refresh token expired remover scheduler.
 *
 * @author Srecko Nikolic
 */
@Component
public class RefreshTokenExpiredRemoverScheduler {

  private RefreshTokenService refreshTokenService;

  private static final Logger logger = LogManager
      .getLogger(RefreshTokenExpiredRemoverScheduler.class);

  /**
   * Instantiates a new Refresh token expired remover scheduler.
   *
   * @param refreshTokenService the refresh token service
   */
  @Autowired
  public RefreshTokenExpiredRemoverScheduler(RefreshTokenService refreshTokenService) {
    this.refreshTokenService = refreshTokenService;
  }

  /**
   * Delete expired refresh tokens.
   */
  @Scheduled(fixedDelay = 3600000)
  public void deleteExpiredRefreshTokens() {
    logger.info("Deleting expired tokens from database");
    refreshTokenService.removeExpiredTokens();
  }
}
