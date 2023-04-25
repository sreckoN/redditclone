package com.srecko.reddit.votes.dto;

import com.srecko.reddit.votes.entity.VoteType;
import jakarta.validation.constraints.NotNull;

/**
 * The type Vote post dto.
 *
 * @author Srecko Nikolic
 */
public class VotePostRequest extends VoteRequest {

  @NotNull
  private Long postId;

  /**
   * Instantiates a new Vote post dto.
   *
   * @param type   the type
   * @param postId the post id
   */
  public VotePostRequest(VoteType type, Long postId) {
    super(type);
    this.postId = postId;
  }

  /**
   * Instantiates a new Vote post dto.
   */
  public VotePostRequest() {
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
