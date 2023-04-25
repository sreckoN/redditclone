package com.srecko.reddit.votes.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * The type Vote comment.
 *
 * @author Srecko Nikolic
 */
@Entity(name = "VoteComment")
@DiscriminatorValue(value = "Comment")
public class VoteComment extends Vote {

  private Long commentId;

  /**
   * Instantiates a new Vote comment.
   *
   * @param userId    the user id
   * @param type      the type
   * @param commentId the comment id
   */
  public VoteComment(Long userId, VoteType type, Long commentId) {
    super(userId, type);
    this.commentId = commentId;
  }

  /**
   * Instantiates a new Vote comment.
   */
  public VoteComment() {
  }

  /**
   * Gets comment id.
   *
   * @return the comment id
   */
  public Long getCommentId() {
    return commentId;
  }

  /**
   * Sets comment id.
   *
   * @param commentId the comment id
   */
  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }
}