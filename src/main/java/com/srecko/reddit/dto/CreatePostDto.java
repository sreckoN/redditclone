package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Create post dto.
 *
 * @author Srecko Nikolic
 */
public class CreatePostDto {

  @NotNull
  private Long subredditId;

  @NotEmpty
  private String title;

  @NotEmpty
  private String text;

  /**
   * Instantiates a new Create post dto.
   *
   * @param subredditId the subreddit id
   * @param title       the title
   * @param text        the text
   */
  public CreatePostDto(Long subredditId, String title, String text) {
    this.subredditId = subredditId;
    this.title = title;
    this.text = text;
  }

  /**
   * Instantiates a new Create post dto.
   */
  public CreatePostDto() {
  }

  /**
   * Gets subreddit id.
   *
   * @return the subreddit id
   */
  public Long getSubredditId() {
    return subredditId;
  }

  /**
   * Sets subreddit id.
   *
   * @param subredditId the subreddit id
   */
  public void setSubredditId(Long subredditId) {
    this.subredditId = subredditId;
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
