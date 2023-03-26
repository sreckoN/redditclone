package com.srecko.reddit.exception.authentication;

import org.springframework.security.core.AuthenticationException;

/**
 * AccountNotEnabledException is thrown when a user hasn't confirmed its email address.
 *
 * @author Srecko Nikolic
 */
public class AccountDisabledException extends AuthenticationException {

  public AccountDisabledException(String username) {
    super("Account with username " + username
        + " is not enabled. Confirm your email address or register again.");
  }
}