package com.srecko.reddit.exception;

public class InvalidEmailVerificationTokenException extends RuntimeException {

  public InvalidEmailVerificationTokenException() {
    super("The token you have provided is invalid.");
  }
}
