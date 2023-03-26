package com.srecko.reddit.exception.user;

/**
 * The type User not found exception.
 *
 * @author Srecko Nikolic
 */
public class UserNotFoundException extends RuntimeException {

  /**
   * Instantiates a new User not found exception.
   *
   * @param username the username
   */
  public UserNotFoundException(String username) {
    super("User with username " + username + " is not found.");
  }

  /**
   * Instantiates a new User not found exception.
   *
   * @param id the id
   */
  public UserNotFoundException(Long id) {
    super("User with id " + id + " is not found");
  }
}
