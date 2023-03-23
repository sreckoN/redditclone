package com.srecko.reddit.exception;

/**
 * The type Refresh token not found exception.
 *
 * @author Srecko Nikolic
 */
public class RefreshTokenNotFoundException extends RuntimeException {

  /**
   * Instantiates a new Refresh token not found exception.
   */
  public RefreshTokenNotFoundException() {
    super("Refresh token not found.");
  }
}
