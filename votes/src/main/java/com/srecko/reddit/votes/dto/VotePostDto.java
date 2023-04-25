package com.srecko.reddit.votes.dto;

import com.srecko.reddit.votes.entity.VoteType;

/**
 * The type Vote post dto.
 *
 * @author Srecko Nikolic
 */
public class VotePostDto {

  private Long id;

  private Long userId;

  private Long postId;

  private VoteType type;

  /**
   * Instantiates a new Vote post dto.
   */
  public VotePostDto() {
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
