package com.srecko.reddit.exception;

import com.srecko.reddit.dto.ExceptionResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Global exception handler.
 *
 * @author Srecko Nikolic
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /*@ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception exception) {
      return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(),
      LocalDateTime.now()),
      HttpStatus.BAD_GATEWAY);
  }*/

  /**
   * Handle authorization header missing response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(AuthorizationHeaderMissingException.class)
  public ResponseEntity<?> handleAuthorizationHeaderMissing(
      AuthorizationHeaderMissingException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handle subreddit not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(SubredditNotFoundException.class)
  public ResponseEntity<?> handleSubredditNotFoundException(SubredditNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle post not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(PostNotFoundException.class)
  public ResponseEntity<?> handlePostNotFoundException(PostNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle user not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle comment not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(CommentNotFoundException.class)
  public ResponseEntity<?> handleCommentNotFoundException(CommentNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle dto validation exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(DtoValidationException.class)
  public ResponseEntity<?> handleDtoValidationException(DtoValidationException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle username not available exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(UsernameNotAvailableException.class)
  public ResponseEntity<?> handleUsernameNotAvailableException(
      UsernameNotAvailableException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle email already in use exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(EmailAlreadyInUseException.class)
  public ResponseEntity<?> handleEmailAlreadyInUseException(EmailAlreadyInUseException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle vote not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(VoteNotFoundException.class)
  public ResponseEntity<?> handleVoteNotFoundException(VoteNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle email validation token expired exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(EmailVerificationTokenExpiredException.class)
  public ResponseEntity<?> handleEmailValidationTokenExpiredException(
      EmailVerificationTokenExpiredException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle invalid email verification token exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(InvalidEmailVerificationTokenException.class)
  public ResponseEntity<?> handleInvalidEmailVerificationTokenException(
      InvalidEmailVerificationTokenException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle email verification token not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(EmailVerificationTokenNotFoundException.class)
  public ResponseEntity<?> handleEmailVerificationTokenNotFoundException(
      EmailVerificationTokenNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle verification email sending error exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(VerificationEmailSendingErrorException.class)
  public ResponseEntity<?> handleVerificationEmailSendingErrorException(
      VerificationEmailSendingErrorException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle refresh token not found exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(RefreshTokenNotFoundException.class)
  public ResponseEntity<?> handleRefreshTokenNotFoundException(
      RefreshTokenNotFoundException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle refresh token invalid exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(RefreshTokenInvalidException.class)
  public ResponseEntity<?> handleRefreshTokenInvalidException(
      RefreshTokenInvalidException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle registration request invalid exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(RegistrationRequestInvalidException.class)
  public ResponseEntity<?> handleRegistrationRequestInvalidException(
      RegistrationRequestInvalidException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle token refresh request invalid response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(TokenRefreshRequestInvalidException.class)
  public ResponseEntity<?> handleTokenRefreshRequestInvalid(
      TokenRefreshRequestInvalidException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle authentication request invalid exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(AuthenticationRequestInvalidException.class)
  public ResponseEntity<?> handleAuthenticationRequestInvalidException(
      AuthenticationRequestInvalidException exception) {
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }
}
