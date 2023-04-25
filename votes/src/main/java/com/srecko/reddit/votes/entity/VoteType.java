package com.srecko.reddit.votes.entity;

/**
 * The enum Vote type.
 *
 * @author Srecko Nikolic
 */
public enum VoteType {
  /**
   * Upvote vote type.
   */
  UPVOTE(1),
  /**
   * Downvote vote type.
   */
  DOWNVOTE(-1);

  private final int val;

  VoteType(int val) {
    this.val = val;
  }

  /**
   * Gets val.
   *
   * @return the val
   */
  public int getVal() {
    return val;
  }
}