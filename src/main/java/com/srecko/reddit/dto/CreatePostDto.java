package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreatePostDto {

  @NotNull
  private Long subredditId;
  @NotEmpty
  private String title;
  @NotEmpty
  private String text;

  public CreatePostDto(Long subredditId, String title, String text) {
    this.subredditId = subredditId;
    this.title = title;
    this.text = text;
  }

  public CreatePostDto() {
  }

  public Long getSubredditId() {
    return subredditId;
  }

  public void setSubredditId(Long subredditId) {
    this.subredditId = subredditId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
