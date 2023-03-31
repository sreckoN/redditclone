package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Refresh token not found exception.
 *
 * @author Srecko Nikolic
 */
public class RefreshTokenNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(RefreshTokenNotFoundException.class);

  /**
   * Instantiates a new Refresh token not found exception.
   */
  public RefreshTokenNotFoundException() {
    super("Refresh token not found.");
    logger.error("Refresh token not found");
  }
}
