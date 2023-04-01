package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Refresh token invalid exception.
 *
 * @author Srecko Nikolic
 */
public class RefreshTokenInvalidException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(RefreshTokenInvalidException.class);

  /**
   * Instantiates a new Refresh token invalid exception.
   */
  public RefreshTokenInvalidException() {
    super("Refresh token is invalid. Log in again.");
    logger.error("Invalid refresh token");
  }
}
