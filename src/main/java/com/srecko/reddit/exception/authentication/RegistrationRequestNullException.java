package com.srecko.reddit.exception.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Registration request null exception.
 *
 * @author Srecko Nikolic
 */
public class RegistrationRequestNullException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(RegistrationRequestNullException.class);

  /**
   * Instantiates a new Registration request null exception.
   */
  public RegistrationRequestNullException() {
    super("RegistrationRequest can't be null.");
    logger.error("Null registration request");
  }
}