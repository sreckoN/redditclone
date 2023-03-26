package com.srecko.reddit.exception.authentication;

/**
 * The type Email already in use exception.
 *
 * @author Srecko Nikolic
 */
public class EmailAlreadyInUseException extends RuntimeException {

  /**
   * Instantiates a new Email already in use exception.
   *
   * @param email the email
   */
  public EmailAlreadyInUseException(String email) {
    super("Email " + email + " is already in use");
  }
}
