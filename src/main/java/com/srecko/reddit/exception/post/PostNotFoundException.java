package com.srecko.reddit.exception.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Post not found exception.
 *
 * @author Srecko Nikolic
 */
public class PostNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(PostNotFoundException.class);

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
    logger.error("Post not found: {}", id);
  }
}
