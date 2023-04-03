package com.srecko.reddit.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import java.util.Date;
import java.util.Objects;

/**
 * The type Comment dto.
 *
 * @author Srecko Nikolic
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CommentDto {

  private Long id;

  private String text;

  private int votes;

  private Date created;

  @JsonIdentityReference(alwaysAsId = true)
  private User user;

  @JsonIdentityReference(alwaysAsId = true)
  private Post post;

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
        && Objects.equals(user.getId(), that.user.getId()) && Objects.equals(post.getId(),
        that.post.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, votes, created, user, post);
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
   * Gets user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets user.
   *
   * @param user the user
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Gets post.
   *
   * @return the post
   */
  public Post getPost() {
    return post;
  }

  /**
   * Sets post.
   *
   * @param post the post
   */
  public void setPost(Post post) {
    this.post = post;
  }
}
