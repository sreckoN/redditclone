package com.srecko.reddit.votes.dto;

import com.srecko.reddit.votes.entity.VoteType;

/**
 * The type Vote comment dto.
 *
 * @author Srecko Nikolic
 */
public class VoteCommentDto {

  private Long id;

  private Long userId;

  private Long commentId;

  private VoteType type;

  /**
   * Instantiates a new Vote comment dto.
   */
  public VoteCommentDto() {
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * Sets user id.
   *
   * @param userId the user id
   */
  public void setUserId(Long userId) {
    this.userId = userId;
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

  /**
   * Gets type.
   *
   * @return the type
   */
  public VoteType getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(VoteType type) {
    this.type = type;
  }
}
