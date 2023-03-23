package com.srecko.reddit.exception;

/**
 * The type Invalid email verification token exception.
 *
 * @author Srecko Nikolic
 */
public class InvalidEmailVerificationTokenException extends RuntimeException {

  /**
   * Instantiates a new Invalid email verification token exception.
   */
  public InvalidEmailVerificationTokenException() {
    super("The token you have provided is invalid.");
  }
}
