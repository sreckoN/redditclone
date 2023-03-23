package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "VoteComment")
@DiscriminatorValue(value = "Comment")
public class VoteComment extends Vote {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "COMMENT_ID_FK"))
  @JsonIdentityReference(alwaysAsId = true)
  private Comment comment;

  public VoteComment(User user, VoteType type, Comment comment) {
    super(user, type);
    this.comment = comment;
  }

  public VoteComment() {
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}