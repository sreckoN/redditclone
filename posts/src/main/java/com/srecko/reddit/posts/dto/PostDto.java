package com.srecko.reddit.posts.dto;

import java.util.Date;
import java.util.Objects;

/**
 * The type Post dto.
 *
 * @author Srecko Nikolic
 */
public class PostDto {

  private Long id;

  private Date dateOfCreation;

  private String title;

  private String text;

  private int votes;

  private int commentsCounter;

  private Long userId;

  private Long subredditId;

  /**
   * Instantiates a new Post dto.
   */
  public PostDto() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostDto postDto = (PostDto) o;
    return id.equals(postDto.id) && Objects.equals(dateOfCreation, postDto.dateOfCreation)
        && title.equals(postDto.title) && text.equals(postDto.text) && userId.equals(postDto.userId)
        && subredditId.equals(postDto.subredditId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateOfCreation, title, text, userId, subredditId);
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
   * Gets date of creation.
   *
   * @return the date of creation
   */
  public Date getDateOfCreation() {
    return dateOfCreation;
  }

  /**
   * Sets date of creation.
   *
   * @param dateOfCreation the date of creation
   */
  public void setDateOfCreation(Date dateOfCreation) {
    this.dateOfCreation = dateOfCreation;
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

  /**
   * Gets votes.
   *
   * @return the votes
   */
  public int getVotes() {
    return votes;
  }

  /**
   * Sets votes.
   *
   * @param votes the votes
   */
  public void setVotes(int votes) {
    this.votes = votes;
  }

  /**
   * Gets comments counter.
   *
   * @return the comments counter
   */
  public int getCommentsCounter() {
    return commentsCounter;
  }

  /**
   * Sets comments counter.
   *
   * @param commentsCounter the comments counter
   */
  public void setCommentsCounter(int commentsCounter) {
    this.commentsCounter = commentsCounter;
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
}
