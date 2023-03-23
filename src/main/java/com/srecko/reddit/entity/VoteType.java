package com.srecko.reddit.entity;

public enum VoteType {
  UPVOTE(1), DOWNVOTE(-1);

  private final int val;

  VoteType(int val) {
    this.val = val;
  }

  public int getVal() {
    return val;
  }
}