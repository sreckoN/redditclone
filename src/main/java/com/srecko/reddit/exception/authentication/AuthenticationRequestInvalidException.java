package com.srecko.reddit.exception.authentication;

import com.srecko.reddit.exception.util.DtoValidationMessageCreator;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * The type Authentication request invalid exception.
 */
public class AuthenticationRequestInvalidException extends RuntimeException {

  /**
   * Instantiates a new Authentication request invalid exception.
   *
   * @param errors the errors
   */
  public AuthenticationRequestInvalidException(List<ObjectError> errors) {
    super(createMessage(errors));
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("Authentication request validation failed for the following fields: ", errors);
  }
}
