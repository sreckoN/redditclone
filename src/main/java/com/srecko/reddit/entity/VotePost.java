package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "VotePost")
@DiscriminatorValue(value = "Post")
public class VotePost extends Vote {

  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "POST_ID_FK"))
  private Post post;

  public VotePost(User user, VoteType type, Post post) {
    super(user, type);
    this.post = post;
  }

  public VotePost() {
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }
}