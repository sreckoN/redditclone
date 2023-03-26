package com.srecko.reddit.exception.authentication;

import com.srecko.reddit.exception.util.DtoValidationMessageCreator;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * The type Registration request exception.
 *
 * @author Srecko Nikolic
 */
public class RegistrationRequestInvalidException extends RuntimeException {

  /**
   * Instantiates a new Registration request exception.
   *
   * @param errors the errors
   */
  public RegistrationRequestInvalidException(List<ObjectError> errors) {
    super(createMessage(errors));
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("Registration request validation failed for the following fields: ", errors);
  }
}
