package com.srecko.reddit.exception.post;

/**
 * The type Post not found exception.
 *
 * @author Srecko Nikolic
 */
public class PostNotFoundException extends RuntimeException {

  /**
   * Instantiates a new Post not found exception.
   *
   * @param message the message
   */
  public PostNotFoundException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Post not found exception.
   *
   * @param id the id
   */
  public PostNotFoundException(Long id) {
    super("Post with id " + id + " is not found.");
  }
}
