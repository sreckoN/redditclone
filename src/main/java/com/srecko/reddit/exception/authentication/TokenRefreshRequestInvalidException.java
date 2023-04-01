package com.srecko.reddit.exception.authentication;

import com.srecko.reddit.exception.util.DtoValidationMessageCreator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.ObjectError;

/**
 * The type Token refresh request invalid exception.
 *
 * @author Srecko Nikolic
 */
public class TokenRefreshRequestInvalidException extends RuntimeException {

  private static final Logger logger = LogManager
      .getLogger(TokenRefreshRequestInvalidException.class);

  /**
   * Instantiates a new Token refresh request invalid exception.
   *
   * @param errors the errors
   */
  public TokenRefreshRequestInvalidException(List<ObjectError> errors) {
    super(createMessage(errors));
    logger.error("Invalid token refresh request");
  }

  private static String createMessage(List<ObjectError> errors) {
    return DtoValidationMessageCreator
        .getMessage("Token refresh request validation failed for the following fields: ", errors);
  }
}
