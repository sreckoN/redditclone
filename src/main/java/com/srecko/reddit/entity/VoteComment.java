package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * The type Vote comment.
 *
 * @author Srecko Nikolic
 */
@Entity(name = "VoteComment")
@DiscriminatorValue(value = "Comment")
public class VoteComment extends Vote {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "COMMENT_ID_FK"))
  @JsonIdentityReference(alwaysAsId = true)
  private Comment comment;

  /**
   * Instantiates a new Vote comment.
   *
   * @param user    the user
   * @param type    the type
   * @param comment the comment
   */
  public VoteComment(User user, VoteType type, Comment comment) {
    super(user, type);
    this.comment = comment;
  }

  /**
   * Instantiates a new Vote comment.
   */
  public VoteComment() {
  }

  /**
   * Gets comment.
   *
   * @return the comment
   */
  public Comment getComment() {
    return comment;
  }

  /**
   * Sets comment.
   *
   * @param comment the comment
   */
  public void setComment(Comment comment) {
    this.comment = comment;
  }
}