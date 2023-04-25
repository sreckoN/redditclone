package com.srecko.reddit.votes.exception;

import com.srecko.reddit.votes.exception.utils.DtoValidationMessageCreator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.ObjectError;

/**
 * The type Dto validation exception.
 *
 * @author Srecko Nikolic
 */
public class DtoValidationException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(DtoValidationException.class);

  /**
   * Instantiates a new Dto validation exception.
   *
   * @param errors the errors
   */
  public DtoValidationException(List<ObjectError> errors) {
    super(createMessage(errors));
    logger.error("DTO validation error");
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("DTO validation failed for the following fields: ", errors);
  }
}
