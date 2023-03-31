package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Username not available exception.
 *
 * @author Srecko Nikolic
 */
public class UsernameNotAvailableException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(UsernameNotAvailableException.class);

  /**
   * Instantiates a new Username not available exception.
   *
   * @param username the username
   */
  public UsernameNotAvailableException(String username) {
    super("Username " + username + " is already in use.");
    logger.error("Unavailable username");
  }
}
