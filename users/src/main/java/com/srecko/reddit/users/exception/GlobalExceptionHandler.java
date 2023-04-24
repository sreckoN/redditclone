package com.srecko.reddit.users.exception;

import com.srecko.reddit.users.exception.utils.ExceptionResponse;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Global exception handler.
 *
 * @author Srecko Nikolic
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

  /*@ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception exception) {
      return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(),
      LocalDateTime.now()),
      HttpStatus.BAD_GATEWAY);
  }*/

  /**
   * Handle user not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleAuthorizationHeaderMissing(
      UserNotFoundException exception) {
    logger.debug("Handling {}", UserNotFoundException.class);
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handle dto validation exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(DtoValidationException.class)
  public ResponseEntity<?> handleDtoValidationException(DtoValidationException exception) {
    logger.debug("Handling {}", DtoValidationException.class);
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }
}
