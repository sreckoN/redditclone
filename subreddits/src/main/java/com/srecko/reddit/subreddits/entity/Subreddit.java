package com.srecko.reddit.subreddits.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * The type Subreddit.
 *
 * @author Srecko Nikolic
 */
@Entity
@Table(name = "subreddits")
public class Subreddit {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(min = 3, max = 50)
  private String name;

  @NotEmpty
  private String description;

  private Date createdDate = new Date();

  private int numberOfUsers;

  @NotNull
  private Long creatorId;

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
   * @param creatorId   the creator id
   */
  public Subreddit(String name, String description, Long creatorId) {
    this.name = name;
    this.description = description;
    this.creatorId = creatorId;
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
   * Gets number of users.
   *
   * @return the number of users
   */
  public int getNumberOfUsers() {
    return numberOfUsers;
  }

  /**
   * Sets number of users.
   *
   * @param numberOfUsers the number of users
   */
  public void setNumberOfUsers(int numberOfUsers) {
    this.numberOfUsers = numberOfUsers;
  }

  /**
   * Gets creator id.
   *
   * @return the creator id
   */
  public Long getCreatorId() {
    return creatorId;
  }

  /**
   * Sets creator id.
   *
   * @param creatorId the creator id
   */
  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }
}