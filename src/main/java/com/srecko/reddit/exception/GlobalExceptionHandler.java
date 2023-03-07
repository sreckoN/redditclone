package com.srecko.reddit.exception;

import com.srecko.reddit.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.BAD_GATEWAY);
    }*/

    @ExceptionHandler(AuthorizationHeaderMissingException.class)
    public ResponseEntity<?> handleAuthorizationHeaderMissing(AuthorizationHeaderMissingException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SubredditNotFoundException.class)
    public ResponseEntity<?> handleSubredditNotFoundException(SubredditNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> handlePostNotFoundException(PostNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<?> handleCommentNotFoundException(CommentNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DtoValidationException.class)
    public ResponseEntity<?> handleDtoValidationException(DtoValidationException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotAvailableException.class)
    public ResponseEntity<?> handleUsernameNotAvailableException(UsernameNotAvailableException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<?> handleEmailAlreadyInUseException(EmailAlreadyInUseException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoteNotFoundException.class)
    public ResponseEntity<?> handleVoteNotFoundException(VoteNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailVerificationTokenExpiredException.class)
    public ResponseEntity<?> handleEmailValidationTokenExpiredException(EmailVerificationTokenExpiredException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmailVerificationTokenException.class)
    public ResponseEntity<?> handleInvalidEmailVerificationTokenException(InvalidEmailVerificationTokenException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailVerificationTokenNotFoundException.class)
    public ResponseEntity<?> handleEmailVerificationTokenNotFoundException(EmailVerificationTokenNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VerificationEmailSendingErrorException.class)
    public ResponseEntity<?> handleVerificationEmailSendingErrorException(VerificationEmailSendingErrorException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<?> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<?> handleRefreshTokenInvalidException(RefreshTokenInvalidException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
