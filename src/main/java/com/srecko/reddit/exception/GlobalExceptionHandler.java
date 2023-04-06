package com.srecko.reddit.exception;

import com.srecko.reddit.dto.responses.ExceptionResponse;
import com.srecko.reddit.exception.authentication.AuthenticationRequestInvalidException;
import com.srecko.reddit.exception.authentication.AuthorizationHeaderMissingException;
import com.srecko.reddit.exception.authentication.EmailAlreadyInUseException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenExpiredException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenInvalidException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenNotFoundException;
import com.srecko.reddit.exception.authentication.RefreshTokenInvalidException;
import com.srecko.reddit.exception.authentication.RefreshTokenNotFoundException;
import com.srecko.reddit.exception.authentication.RegistrationRequestInvalidException;
import com.srecko.reddit.exception.authentication.TokenRefreshRequestInvalidException;
import com.srecko.reddit.exception.authentication.UsernameNotAvailableException;
import com.srecko.reddit.exception.authentication.VerificationEmailSendingErrorException;
import com.srecko.reddit.exception.comment.CommentNotFoundException;
import com.srecko.reddit.exception.post.PostNotFoundException;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.exception.vote.VoteNotFoundException;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
   * Handle authorization header missing response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(AuthorizationHeaderMissingException.class)
  public ResponseEntity<?> handleAuthorizationHeaderMissing(
      AuthorizationHeaderMissingException exception) {
    logger.debug("Handling {}", AuthorizationHeaderMissingException.class);
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
    logger.debug("Handling {}", SubredditNotFoundException.class);
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
    logger.debug("Handling {}", PostNotFoundException.class);
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
    logger.debug("Handling {}", UserNotFoundException.class);
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
    logger.debug("Handling {}", CommentNotFoundException.class);
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
    logger.debug("Handling {}", DtoValidationException.class);
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
    logger.debug("Handling {}", UsernameNotAvailableException.class);
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
    logger.debug("Handling {}", EmailAlreadyInUseException.class);
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
    logger.debug("Handling {}", VoteNotFoundException.class);
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
    logger.debug("Handling {}", EmailVerificationTokenExpiredException.class);
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handle invalid email verification token exception response entity.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(EmailVerificationTokenInvalidException.class)
  public ResponseEntity<?> handleInvalidEmailVerificationTokenException(
      EmailVerificationTokenInvalidException exception) {
    logger.debug("Handling {}", EmailVerificationTokenInvalidException.class);
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
    logger.debug("Handling {}", EmailVerificationTokenNotFoundException.class);
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
    logger.debug("Handling {}", VerificationEmailSendingErrorException.class);
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
    logger.debug("Handling {}", RefreshTokenNotFoundException.class);
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
    logger.debug("Handling {}", RefreshTokenInvalidException.class);
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
    logger.debug("Handling {}", RegistrationRequestInvalidException.class);
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
    logger.debug("Handling {}", TokenRefreshRequestInvalidException.class);
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
    logger.debug("Handling {}", AuthenticationRequestInvalidException.class);
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()),
        HttpStatus.NOT_FOUND);
  }
}
