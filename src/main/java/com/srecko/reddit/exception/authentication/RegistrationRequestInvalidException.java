package com.srecko.reddit.exception.authentication;

import com.srecko.reddit.exception.util.DtoValidationMessageCreator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.ObjectError;

/**
 * The type Registration request exception.
 *
 * @author Srecko Nikolic
 */
public class RegistrationRequestInvalidException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(RegistrationRequestInvalidException.class);

  /**
   * Instantiates a new Registration request exception.
   *
   * @param errors the errors
   */
  public RegistrationRequestInvalidException(List<ObjectError> errors) {
    super(createMessage(errors));
    logger.error("Invalid registration request");
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("Registration request validation failed for the following fields: ", errors);
  }
}
