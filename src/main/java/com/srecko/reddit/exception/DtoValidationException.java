package com.srecko.reddit.exception;

import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class DtoValidationException extends RuntimeException {

  public DtoValidationException(List<ObjectError> errors) {
    super(createMessage(errors));
  }

  private static String createMessage(List<ObjectError> errors) {
    String message = "DTO validation failed for the following fields: ";
    for (int i = 0; i < errors.size(); i++) {
      String fieldError = ((FieldError) errors.get(i)).getField();
      if (i == errors.size() - 1) {
        message += fieldError + ".";
      } else {
        message += fieldError + ", ";
      }
    }
    return message;
  }
}
