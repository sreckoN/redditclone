package com.srecko.reddit.exception;

/**
 * The type Comment not found exception.
 *
 * @author Srecko Nikolic
 */
public class CommentNotFoundException extends RuntimeException {

  /**
   * Instantiates a new Comment not found exception.
   *
   * @param id the id
   */
  public CommentNotFoundException(Long id) {
    super("Comment with id " + id + " is not found.");
  }
}
