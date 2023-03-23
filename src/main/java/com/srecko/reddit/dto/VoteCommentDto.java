package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

public class VoteCommentDto extends VoteDto {

  @NotNull
  private Long commentId;

  public VoteCommentDto(VoteType type, Long commentId) {
    super(type);
    this.commentId = commentId;
  }

  public VoteCommentDto() {
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }
}
