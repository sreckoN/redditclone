package com.srecko.reddit.exception;

/**
 * The type Username not available exception.
 *
 * @author Srecko Nikolic
 */
public class UsernameNotAvailableException extends RuntimeException {

  /**
   * Instantiates a new Username not available exception.
   *
   * @param username the username
   */
  public UsernameNotAvailableException(String username) {
    super("Username " + username + " is already in use.");
  }
}
