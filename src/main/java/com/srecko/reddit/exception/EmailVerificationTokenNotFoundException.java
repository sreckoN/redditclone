package com.srecko.reddit.exception;

/**
 * The type Email verification token not found exception.
 *
 * @author Srecko Nikolic
 */
public class EmailVerificationTokenNotFoundException extends RuntimeException {

  /**
   * Instantiates a new Email verification token not found exception.
   */
  public EmailVerificationTokenNotFoundException() {
    super("Verification token is not found.");
  }
}
