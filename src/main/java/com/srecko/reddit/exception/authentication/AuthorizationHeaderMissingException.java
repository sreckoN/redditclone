package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Authorization header missing exception.
 *
 * @author Srecko Nikolic
 */
public class AuthorizationHeaderMissingException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(AuthorizationHeaderMissingException.class);

  /**
   * Instantiates a new Authorization header missing exception.
   *
   * @param message the message
   */
  public AuthorizationHeaderMissingException(String message) {
    super(message);
    logger.error("Authorization header missing");
  }
}
