package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

/**
 * The type Vote comment dto.
 *
 * @author Srecko Nikolic
 */
public class VoteCommentDto extends VoteDto {

  @NotNull
  private Long commentId;

  /**
   * Instantiates a new Vote comment dto.
   *
   * @param type      the type
   * @param commentId the comment id
   */
  public VoteCommentDto(VoteType type, Long commentId) {
    super(type);
    this.commentId = commentId;
  }

  /**
   * Instantiates a new Vote comment dto.
   */
  public VoteCommentDto() {
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
