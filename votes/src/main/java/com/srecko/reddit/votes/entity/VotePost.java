package com.srecko.reddit.votes.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * The type Vote post.
 *
 * @author Srecko Nikolic
 */
@Entity(name = "VotePost")
@DiscriminatorValue(value = "Post")
public class VotePost extends Vote {

  private Long postId;

  /**
   * Instantiates a new Vote post.
   *
   * @param userId the user id
   * @param type   the type
   * @param postId the post id
   */
  public VotePost(Long userId, VoteType type, Long postId) {
    super(userId, type);
    this.postId = postId;
  }

  /**
   * Instantiates a new Vote post.
   */
  public VotePost() {
  }

  /**
   * Gets post id.
   *
   * @return the post id
   */
  public Long getPostId() {
    return postId;
  }

  /**
   * Sets post id.
   *
   * @param postId the post id
   */
  public void setPostId(Long postId) {
    this.postId = postId;
  }
}