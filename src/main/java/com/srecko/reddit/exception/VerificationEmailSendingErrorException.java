package com.srecko.reddit.exception;

/**
 * The type Verification email sending error exception.
 *
 * @author Srecko Nikolic
 */
public class VerificationEmailSendingErrorException extends RuntimeException {

  /**
   * Instantiates a new Verification email sending error exception.
   *
   * @param message the message
   */
  public VerificationEmailSendingErrorException(String message) {
    super(message);
  }
}
