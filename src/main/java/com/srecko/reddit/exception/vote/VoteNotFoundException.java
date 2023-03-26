package com.srecko.reddit.exception.vote;

/**
 * The type Vote not found exception.
 *
 * @author Srecko Nikolic
 */
public class VoteNotFoundException extends RuntimeException {

  /**
   * Instantiates a new Vote not found exception.
   *
   * @param id the id
   */
  public VoteNotFoundException(Long id) {
    super("Vote with id " + id + " is not found.");
  }
}