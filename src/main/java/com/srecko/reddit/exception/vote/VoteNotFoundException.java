package com.srecko.reddit.exception.vote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Vote not found exception.
 *
 * @author Srecko Nikolic
 */
public class VoteNotFoundException extends RuntimeException {

  private static final Logger logger = LogManager.getLogger(VoteNotFoundException.class);

  /**
   * Instantiates a new Vote not found exception.
   *
   * @param id the id
   */
  public VoteNotFoundException(Long id) {
    super("Vote with id " + id + " is not found.");
    logger.error("Vote not found: {}", id);
  }
}
