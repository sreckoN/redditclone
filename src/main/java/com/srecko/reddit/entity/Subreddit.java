package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Subreddit.
 *
 * @author Srecko Nikolic
 */
@Entity
@Table(name = "subreddits")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Subreddit {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(min = 3, max = 50)
  private String name;

  private String description;

  private Date createdDate = new Date();

  private int numberOfUsers;

  @NotNull
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "USER_ID_FK"))
  private User creator;

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "subreddit",
      orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Post> posts = new ArrayList<>();

  /**
   * Instantiates a new Subreddit.
   */
  public Subreddit() {
  }

  /**
   * Instantiates a new Subreddit.
   *
   * @param name        the name
   * @param description the description
   * @param creator     the creator
   */
  public Subreddit(String name, String description, User creator) {
    this.name = name;
    this.description = description;
    this.creator = creator;
    this.numberOfUsers = 0;
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
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets created date.
   *
   * @return the created date
   */
  public Date getCreatedDate() {
    return createdDate;
  }

  /**
   * Sets created date.
   *
   * @param createdDate the created date
   */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Gets creator.
   *
   * @return the creator
   */
  public User getCreator() {
    return creator;
  }

  /**
   * Sets creator.
   *
   * @param creator the creator
   */
  public void setCreator(User creator) {
    this.creator = creator;
  }

  /**
   * Gets posts.
   *
   * @return the posts
   */
  public List<Post> getPosts() {
    return posts;
  }

  /**
   * Sets posts.
   *
   * @param posts the posts
   */
  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}