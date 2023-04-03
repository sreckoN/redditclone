package com.srecko.reddit.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Comment dto.
 *
 * @author Srecko Nikolic
 */
public class CommentRequest {

  @NotEmpty
  private String text;

  @NotNull
  private Long postId;

  /**
   * Instantiates a new Comment dto.
   *
   * @param text the text
   * @param post the post
   */
  public CommentRequest(String text, Long post) {
    this.text = text;
    this.postId = post;
  }

  /**
   * Instantiates a new Comment dto.
   */
  public CommentRequest() {
  }

  /**
   * Gets text.
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Sets text.
   *
   * @param text the text
   */
  public void setText(String text) {
    this.text = text;
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
