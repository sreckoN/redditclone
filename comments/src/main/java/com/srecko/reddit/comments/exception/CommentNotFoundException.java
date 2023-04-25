package com.srecko.reddit.comments.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Comment not found exception.
 *
 * @author Srecko Nikolic
 */
public class CommentNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(CommentNotFoundException.class);

  /**
   * Instantiates a new Comment not found exception.
   *
   * @param id the id
   */
  public CommentNotFoundException(Long id) {
    super("Comment with id " + id + " is not found.");
    logger.error("Comment not found: {}", id);
  }
}
