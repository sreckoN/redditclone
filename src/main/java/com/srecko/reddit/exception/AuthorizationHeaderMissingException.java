package com.srecko.reddit.exception;

public class AuthorizationHeaderMissingException extends RuntimeException {

  public AuthorizationHeaderMissingException(String message) {
    super(message);
  }
}
