package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Email verification token not found exception.
 *
 * @author Srecko Nikolic
 */
public class EmailVerificationTokenNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(EmailVerificationTokenNotFoundException.class);

  /**
   * Instantiates a new Email verification token not found exception.
   */
  public EmailVerificationTokenNotFoundException() {
    super("Verification token is not found.");
    logger.error("Email verification token not found");
  }
}
