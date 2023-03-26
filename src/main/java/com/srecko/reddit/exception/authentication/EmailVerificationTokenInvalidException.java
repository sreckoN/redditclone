package com.srecko.reddit.exception.authentication;

/**
 * The type Invalid email verification token exception.
 *
 * @author Srecko Nikolic
 */
public class EmailVerificationTokenInvalidException extends RuntimeException {

  /**
   * Instantiates a new Invalid email verification token exception.
   */
  public EmailVerificationTokenInvalidException() {
    super("The token you have provided is invalid.");
  }
}
