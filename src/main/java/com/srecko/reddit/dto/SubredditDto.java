package com.srecko.reddit.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.srecko.reddit.entity.User;
import java.util.Date;
import java.util.Objects;

/**
 * The type Subreddit dto.
 *
 * @author Srecko Nikolic
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SubredditDto {

  private Long id;

  private String name;

  private String description;

  private Date createdDate;

  private int numberOfUsers;

  @JsonIdentityReference(alwaysAsId = true)
  private User creator;

  /**
   * Instantiates a new Subreddit dto.
   */
  public SubredditDto() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubredditDto that = (SubredditDto) o;
    return id.equals(that.id) && name.equals(that.name) && description.equals(that.description)
        && Objects.equals(createdDate, that.createdDate) && creator.equals(that.creator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, createdDate, creator);
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
}
