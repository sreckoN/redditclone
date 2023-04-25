package com.srecko.reddit.posts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * The type Post.
 *
 * @author Srecko Nikolic
 */
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue
  private Long id;

  private Date dateOfCreation;

  @NotEmpty
  @Size(min = 3, max = 35)
  private String title;

  @NotEmpty
  @Size(min = 2, max = 1000)
  private String text;

  private int votes;

  private int commentsCounter;

  private Long userId;

  private Long subredditId;

  /**
   * Instantiates a new Post.
   */
  public Post() {
  }

  /**
   * Instantiates a new Post.
   *
   * @param createdBy   the created by
   * @param title       the title
   * @param text        the text
   * @param subredditId the subreddit id
   */
  public Post(Long createdBy, String title, String text, Long subredditId) {
    this.userId = createdBy;
    this.title = title;
    this.text = text;
    this.subredditId = subredditId;
    this.dateOfCreation = new Date();
    this.votes = 0;
    this.commentsCounter = 0;
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
   * Gets user.
   *
   * @return the user
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * Sets user.
   *
   * @param userId the user id
   */
  public void setUserId(Long userId) {
    this.userId = userId;
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
   * Gets subreddit.
   *
   * @return the subreddit
   */
  public Long getSubredditId() {
    return subredditId;
  }

  /**
   * Sets subreddit.
   *
   * @param subredditId the subreddit id
   */
  public void setSubredditId(Long subredditId) {
    this.subredditId = subredditId;
  }
}
