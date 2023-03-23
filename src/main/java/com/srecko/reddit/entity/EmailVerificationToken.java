package com.srecko.reddit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * The type Email verification token.
 *
 * @author Srecko Nikolic
 */
@Entity
public class EmailVerificationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty
  private String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  @NotNull
  private User user;

  @NotNull
  private Date expiryDate;

  /**
   * Instantiates a new Email verification token.
   */
  public EmailVerificationToken() {
  }

  /**
   * Instantiates a new Email verification token.
   *
   * @param user the user
   */
  public EmailVerificationToken(User user) {
    this.user = user;
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime futureTime = currentTime.plus(24, ChronoUnit.HOURS);
    this.expiryDate = Date.from(futureTime.atZone(ZoneId.systemDefault()).toInstant());
    this.token = UUID.randomUUID().toString();
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
   * Gets token.
   *
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets token.
   *
   * @param token the token
   */
  public void setToken(String token) {
    this.token = token;
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
   * Gets expiry date.
   *
   * @return the expiry date
   */
  public Date getExpiryDate() {
    return expiryDate;
  }

  /**
   * Sets expiry date.
   *
   * @param expiryDate the expiry date
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }
}