package com.srecko.reddit.exception;

import com.srecko.reddit.exception.util.DtoValidationMessageCreator;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * The type Dto validation exception.
 *
 * @author Srecko Nikolic
 */
public class DtoValidationException extends RuntimeException {

  /**
   * Instantiates a new Dto validation exception.
   *
   * @param errors the errors
   */
  public DtoValidationException(List<ObjectError> errors) {
    super(createMessage(errors));
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("DTO validation failed for the following fields: ", errors);
  }
}
