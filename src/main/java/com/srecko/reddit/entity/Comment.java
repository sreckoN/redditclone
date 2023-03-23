package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(min = 2, max = 500)
  private String text;

  private int votes;

  private Date created;

  @JsonIdentityReference(alwaysAsId = true)
  @NotNull
  @ManyToOne
  private User user;

  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne
  private Post post;

  /**
   * Instantiates a new Comment.
   */
  public Comment() {
  }

  /**
   * Instantiates a new Comment.
   *
   * @param user the user
   * @param text the text
   * @param post the post
   */
  public Comment(User user, String text, Post post) {
    this.user = user;
    this.text = text;
    this.post = post;
    this.votes = 0;
    this.created = new Date();
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