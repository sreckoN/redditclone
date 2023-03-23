package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Subreddit dto.
 *
 * @author Srecko Nikolic
 */
public class SubredditDto {

  @NotNull
  private Long subredditId;

  @NotEmpty
  private String name;

  @NotEmpty
  private String description;

  /**
   * Instantiates a new Subreddit dto.
   */
  public SubredditDto() {
  }

  /**
   * Instantiates a new Subreddit dto.
   *
   * @param id          the id
   * @param name        the name
   * @param description the description
   */
  public SubredditDto(Long id, String name, String description) {
    this.subredditId = id;
    this.name = name;
    this.description = description;
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
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
