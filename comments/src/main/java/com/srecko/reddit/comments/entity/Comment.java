package com.srecko.reddit.comments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * The type Comment.
 *
 * @author Srecko Nikolic
 */
@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(min = 2, max = 500)
  private String text;

  private int votes;

  @NotNull
  private Date created;

  @NotNull
  private Long userId;

  @NotNull
  private CommentParentType parentType;

  @NotNull
  private Long parentId;

  private int commentsCounter;

  /**
   * Instantiates a new Comment.
   */
  public Comment() {
  }

  /**
   * Instantiates a new Comment.
   *
   * @param userId     the user id
   * @param text       the text
   * @param parentType the entity type
   * @param entityId   the post id
   */
  public Comment(Long userId, String text, CommentParentType parentType, Long entityId) {
    this.userId = userId;
    this.text = text;
    this.parentType = parentType;
    this.parentId = entityId;
    this.votes = 0;
    this.created = new Date();
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
   * Gets created.
   *
   * @return the created
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets created.
   *
   * @param created the created
   */
  public void setCreated(Date created) {
    this.created = created;
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
  public Long getParentId() {
    return parentId;
  }

  /**
   * Sets post id.
   *
   * @param postId the post id
   */
  public void setParentId(Long postId) {
    this.parentId = postId;
  }

  /**
   * Gets entity type.
   *
   * @return the entity type
   */
  public CommentParentType getParentType() {
    return parentType;
  }

  /**
   * Sets entity type.
   *
   * @param entityType the entity type
   */
  public void setParentType(CommentParentType entityType) {
    this.parentType = entityType;
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
}