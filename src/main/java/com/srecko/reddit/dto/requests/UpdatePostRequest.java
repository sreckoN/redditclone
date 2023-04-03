package com.srecko.reddit.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Update post dto.
 *
 * @author Srecko Nikolic
 */
public class UpdatePostRequest {

  @NotNull
  private Long postId;

  @NotEmpty
  private String title;

  @NotEmpty
  private String text;

  /**
   * Instantiates a new Update post dto.
   *
   * @param id    the id
   * @param title the title
   * @param text  the text
   */
  public UpdatePostRequest(Long id, String title, String text) {
    this.postId = id;
    this.title = title;
    this.text = text;
  }

  /**
   * Instantiates a new Update post dto.
   */
  public UpdatePostRequest() {
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

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets title.
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
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
}
