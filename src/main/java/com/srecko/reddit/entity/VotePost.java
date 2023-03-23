package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * The type Vote post.
 *
 * @author Srecko Nikolic
 */
@Entity(name = "VotePost")
@DiscriminatorValue(value = "Post")
public class VotePost extends Vote {

  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "POST_ID_FK"))
  private Post post;

  /**
   * Instantiates a new Vote post.
   *
   * @param user the user
   * @param type the type
   * @param post the post
   */
  public VotePost(User user, VoteType type, Post post) {
    super(user, type);
    this.post = post;
  }

  /**
   * Instantiates a new Vote post.
   */
  public VotePost() {
  }

  /**
   * Gets post.
   *
   * @return the post
   */
  public Post getPost() {
    return post;
  }

  /**
   * Sets post.
   *
   * @param post the post
   */
  public void setPost(Post post) {
    this.post = post;
  }
}