package com.srecko.reddit.exception.subreddit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Subreddit not found exception.
 *
 * @author Srecko Nikolic
 */
public class SubredditNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(SubredditNotFoundException.class);

  /**
   * Instantiates a new Subreddit not found exception.
   *
   * @param id the id
   */
  public SubredditNotFoundException(Long id) {
    super("Subreddit with id " + id + " is not found.");
    logger.error("Subreddit not found: {}", id);
  }
}
