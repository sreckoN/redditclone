package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Email already in use exception.
 *
 * @author Srecko Nikolic
 */
public class EmailAlreadyInUseException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(EmailAlreadyInUseException.class);

  /**
   * Instantiates a new Email already in use exception.
   *
   * @param email the email
   */
  public EmailAlreadyInUseException(String email) {
    super("Email " + email + " is already in use");
    logger.error("Email is already in use");
  }
}
