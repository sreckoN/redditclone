package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Post.
 *
 * @author Srecko Nikolic
 */
@Entity
@Table(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne
  private User user;

  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne
  private Subreddit subreddit;

  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @JsonIgnore
  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      mappedBy = "post",
      fetch = FetchType.LAZY)
  private List<Comment> comments;

  /**
   * Instantiates a new Post.
   */
  public Post() {
  }

  /**
   * Instantiates a new Post.
   *
   * @param createdBy the created by
   * @param title     the title
   * @param text      the text
   * @param subreddit the subreddit
   */
  public Post(User createdBy, String title, String text, Subreddit subreddit) {
    this.user = createdBy;
    this.title = title;
    this.text = text;
    this.subreddit = subreddit;
    this.dateOfCreation = new Date();
    this.votes = 0;
    this.commentsCounter = 0;
    this.comments = new ArrayList<>();
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
  public Subreddit getSubreddit() {
    return subreddit;
  }

  /**
   * Sets subreddit.
   *
   * @param subreddit the subreddit
   */
  public void setSubreddit(Subreddit subreddit) {
    this.subreddit = subreddit;
  }

  /**
   * Gets comments.
   *
   * @return the comments
   */
  public List<Comment> getComments() {
    return comments;
  }

  /**
   * Sets comments.
   *
   * @param comments the comments
   */
  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
}
