package com.srecko.reddit.comments.dto;

import com.srecko.reddit.comments.entity.CommentParentType;
import java.util.Date;
import java.util.Objects;

/**
 * The type Comment dto.
 *
 * @author Srecko Nikolic
 */
public class CommentDto {

  private Long id;

  private String text;

  private int votes;

  private Date created;

  private Long userId;

  private CommentParentType parentType;

  private Long parentId;

  private int commentsCounter;

  /**
   * Instantiates a new Comment dto.
   */
  public CommentDto() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentDto that = (CommentDto) o;
    return id.equals(that.id) && text.equals(that.text) && Objects.equals(created, that.created)
        && Objects.equals(userId, that.userId) && Objects.equals(parentType, that.parentType)
        && Objects.equals(parentId, that.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, votes, created, userId, parentType, parentId);
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
   * Gets parent type.
   *
   * @return the parent type
   */
  public CommentParentType getParentType() {
    return parentType;
  }

  /**
   * Sets parent type.
   *
   * @param parentType the parent type
   */
  public void setParentType(CommentParentType parentType) {
    this.parentType = parentType;
  }

  /**
   * Gets parent id.
   *
   * @return the parent id
   */
  public Long getParentId() {
    return parentId;
  }

  /**
   * Sets parent id.
   *
   * @param parentId the parent id
   */
  public void setParentId(Long parentId) {
    this.parentId = parentId;
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
