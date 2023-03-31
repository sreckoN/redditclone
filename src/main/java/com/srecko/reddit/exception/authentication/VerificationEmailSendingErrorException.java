package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Verification email sending error exception.
 *
 * @author Srecko Nikolic
 */
public class VerificationEmailSendingErrorException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(VerificationEmailSendingErrorException.class);

  /**
   * Instantiates a new Verification email sending error exception.
   *
   * @param message the message
   */
  public VerificationEmailSendingErrorException(String message) {
    super(message);
    logger.error("Error while sending email");
  }
}
