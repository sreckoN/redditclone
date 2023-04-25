package com.srecko.reddit.votes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * The type Vote.
 *
 * @author Srecko Nikolic
 */
@Entity(name = "Vote")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "votes")
public abstract class Vote {

  @Id
  @GeneratedValue
  private Long id;

  private Long userId;

  private VoteType type;

  /**
   * Instantiates a new Vote.
   *
   * @param userId the user id
   * @param type   the type
   */
  public Vote(Long userId, VoteType type) {
    this.userId = userId;
    this.type = type;
  }

  /**
   * Instantiates a new Vote.
   */
  public Vote() {
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
   * Gets type.
   *
   * @return the type
   */
  public VoteType getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(VoteType type) {
    this.type = type;
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
}