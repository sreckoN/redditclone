package com.srecko.reddit.exception.authentication;

/**
 * The type Authorization header missing exception.
 *
 * @author Srecko Nikolic
 */
public class AuthorizationHeaderMissingException extends RuntimeException {

  /**
   * Instantiates a new Authorization header missing exception.
   *
   * @param message the message
   */
  public AuthorizationHeaderMissingException(String message) {
    super(message);
  }
}
