package com.srecko.reddit.exception;

public class PostNotFoundException extends RuntimeException {

  public PostNotFoundException(String message) {
    super(message);
  }

  public PostNotFoundException(Long id) {
    super("Post with id " + id + " is not found.");
  }
}
