package com.srecko.reddit.exception;

import com.srecko.reddit.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()), HttpStatus.BAD_GATEWAY);
    }

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
}
