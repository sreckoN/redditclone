package com.srecko.reddit.exception.authentication;

/**
 * The type Email verification token expired exception.
 *
 * @author Srecko Nikolic
 */
public class EmailVerificationTokenExpiredException extends RuntimeException {

  /**
   * Instantiates a new Email verification token expired exception.
   */
  public EmailVerificationTokenExpiredException() {
    super("Email Verification token expired. Register again.");
  }
}