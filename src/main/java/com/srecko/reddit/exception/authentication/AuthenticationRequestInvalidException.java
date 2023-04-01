package com.srecko.reddit.exception.authentication;

import com.srecko.reddit.exception.util.DtoValidationMessageCreator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * The type Authentication request invalid exception.
 */
public class AuthenticationRequestInvalidException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(AuthenticationRequestInvalidException.class);

  /**
   * Instantiates a new Authentication request invalid exception.
   *
   * @param errors the errors
   */
  public AuthenticationRequestInvalidException(List<ObjectError> errors) {
    super(createMessage(errors));
    logger.error("Invalid authentication request");
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("Authentication request validation failed for the following fields: ", errors);
  }
}
