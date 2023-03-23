package com.srecko.reddit.exception;

public class RegistrationRequestNullException extends RuntimeException {

  public RegistrationRequestNullException() {
    super("RegistrationRequest can't be null.");
  }
}