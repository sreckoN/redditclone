package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SubredditDto {

  @NotNull
  private Long subredditId;
  @NotEmpty
  private String name;
  @NotEmpty
  private String description;

  public SubredditDto() {
  }

  public SubredditDto(Long id, String name, String description) {
    this.subredditId = id;
    this.name = name;
    this.description = description;
  }

  public Long getSubredditId() {
    return subredditId;
  }

  public void setSubredditId(Long subredditId) {
    this.subredditId = subredditId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
