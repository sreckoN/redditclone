package com.srecko.reddit.users.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type User not found exception.
 *
 * @author Srecko Nikolic
 */
public class UserNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(UserNotFoundException.class);

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
    super("User with id " + id + " is not found.");
    logger.error("User not found: {}", id);
  }
}
