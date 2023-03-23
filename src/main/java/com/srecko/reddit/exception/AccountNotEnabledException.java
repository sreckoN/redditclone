package com.srecko.reddit.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountNotEnabledException extends AuthenticationException {

  public AccountNotEnabledException(String username) {
    super("Account with username " + username
        + " is not enabled. Confirm your email address or register again.");
  }
}