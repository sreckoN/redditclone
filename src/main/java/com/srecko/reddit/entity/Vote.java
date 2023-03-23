package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "Vote")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "votes")
public abstract class Vote {

  @Id
  @GeneratedValue
  private Long id;

  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "USER_ID_FK"))
  private User user;

  private VoteType type;

  public Vote(User user, VoteType type) {
    this.user = user;
    this.type = type;
  }

  public Vote() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public VoteType getType() {
    return type;
  }

  public void setType(VoteType type) {
    this.type = type;
  }
}