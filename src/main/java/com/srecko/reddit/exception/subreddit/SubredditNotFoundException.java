package com.srecko.reddit.exception.subreddit;

/**
 * The type Subreddit not found exception.
 *
 * @author Srecko Nikolic
 */
public class SubredditNotFoundException extends RuntimeException {

  /**
   * Instantiates a new Subreddit not found exception.
   *
   * @param id the id
   */
  public SubredditNotFoundException(Long id) {
    super("Subreddit with id " + id + " is not found.");
  }
}
