package com.srecko.reddit.exception;

public class CommentNotFoundException extends RuntimeException {

  public CommentNotFoundException(Long id) {
    super("Comment with id " + id + " is not found.");
  }
}
