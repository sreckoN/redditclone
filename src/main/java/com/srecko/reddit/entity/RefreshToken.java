package com.srecko.reddit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;

/**
 * The type Refresh token.
 *
 * @author Srecko Nikolic
 */
@Entity(name = "refresh_token")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

  /**
   * Instantiates a new Refresh token.
   *
   * @param user       the user
   * @param token      the token
   * @param expiryDate the expiry date
   */
  public RefreshToken(User user, String token, Instant expiryDate) {
    this.user = user;
    this.token = token;
    this.expiryDate = expiryDate;
  }

  /**
   * Instantiates a new Refresh token.
   */
  public RefreshToken() {
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
   * Gets expiry date.
   *
   * @return the expiry date
   */
  public Instant getExpiryDate() {
    return expiryDate;
  }

  /**
   * Sets expiry date.
   *
   * @param expiryDate the expiry date
   */
  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }
}
