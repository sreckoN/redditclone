package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;

/**
 * AccountNotEnabledException is thrown when a user hasn't confirmed its email address.
 *
 * @author Srecko Nikolic
 */
public class AccountDisabledException extends AuthenticationException {

  private static final Logger logger = LogManager.getLogger(AccountDisabledException.class);

  /**
   * Instantiates a new Account disabled exception.
   *
   * @param username the username
   */
  public AccountDisabledException(String username) {
    super("Account with username " + username
        + " is not enabled. Confirm your email address or register again.");
    logger.error("Account is disabled");
  }
}