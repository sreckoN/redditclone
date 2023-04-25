package com.srecko.reddit.comments.exception.utils;

import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * The type Dto validation message creator.
 */
public class DtoValidationMessageCreator {

  /**
   * Gets message.
   *
   * @param firstLine the first line
   * @param errors    the errors
   * @return the message
   */
  public static String getMessage(String firstLine, List<ObjectError> errors) {
    StringBuilder builder = new StringBuilder(firstLine);
    for (int i = 0; i < errors.size(); i++) {
      String fieldError = ((FieldError) errors.get(i)).getField();
      if (i == errors.size() - 1) {
        builder.append(fieldError).append(".");
      } else {
        builder.append(fieldError).append(", ");
      }
    }
    return builder.toString();
  }
}