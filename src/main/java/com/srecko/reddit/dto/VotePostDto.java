package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

/**
 * The type Vote post dto.
 *
 * @author Srecko Nikolic
 */
public class VotePostDto extends VoteDto {

  @NotNull
  private Long postId;

  /**
   * Instantiates a new Vote post dto.
   *
   * @param type   the type
   * @param postId the post id
   */
  public VotePostDto(VoteType type, Long postId) {
    super(type);
    this.postId = postId;
  }

  /**
   * Instantiates a new Vote post dto.
   */
  public VotePostDto() {
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
