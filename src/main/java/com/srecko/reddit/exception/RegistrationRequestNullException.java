package com.srecko.reddit.exception;

/**
 * The type Registration request null exception.
 *
 * @author Srecko Nikolic
 */
public class RegistrationRequestNullException extends RuntimeException {

  /**
   * Instantiates a new Registration request null exception.
   */
  public RegistrationRequestNullException() {
    super("RegistrationRequest can't be null.");
  }
}