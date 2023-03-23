package com.srecko.reddit.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * AccountNotEnabledException is thrown when a user hasn't confirmed its email address.
 *
 * @author Srecko Nikolic
 */
public class AccountNotEnabledException extends AuthenticationException {

  public AccountNotEnabledException(String username) {
    super("Account with username " + username
        + " is not enabled. Confirm your email address or register again.");
  }
}