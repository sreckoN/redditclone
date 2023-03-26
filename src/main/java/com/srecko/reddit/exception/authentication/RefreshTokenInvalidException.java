package com.srecko.reddit.exception.authentication;

/**
 * The type Refresh token invalid exception.
 *
 * @author Srecko Nikolic
 */
public class RefreshTokenInvalidException extends RuntimeException {

  /**
   * Instantiates a new Refresh token invalid exception.
   */
  public RefreshTokenInvalidException() {
    super("Refresh token is invalid. Log in again.");
  }
}
