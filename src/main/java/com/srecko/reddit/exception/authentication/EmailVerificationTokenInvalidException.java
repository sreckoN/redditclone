package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Invalid email verification token exception.
 *
 * @author Srecko Nikolic
 */
public class EmailVerificationTokenInvalidException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(EmailVerificationTokenInvalidException.class);

  /**
   * Instantiates a new Invalid email verification token exception.
   */
  public EmailVerificationTokenInvalidException() {
    super("The token you have provided is invalid.");
    logger.error("Invalid email verification token");
  }
}
