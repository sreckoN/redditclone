package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Email verification token expired exception.
 *
 * @author Srecko Nikolic
 */
public class EmailVerificationTokenExpiredException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(EmailVerificationTokenExpiredException.class);

  /**
   * Instantiates a new Email verification token expired exception.
   */
  public EmailVerificationTokenExpiredException() {
    super("Email Verification token expired. Register again.");
    logger.error("Email verification token expired");
  }
}