package com.srecko.reddit.comments.dto;

import com.srecko.reddit.comments.entity.CommentParentType;
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
  private CommentParentType parentType;

  @NotNull
  private Long parentId;

  /**
   * Instantiates a new Comment dto.
   *
   * @param text       the text
   * @param parentType the entity type
   * @param parentId   the parent id
   */
  public CommentRequest(String text, CommentParentType parentType, Long parentId) {
    this.text = text;
    this.parentType = parentType;
    this.parentId = parentId;
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
}
