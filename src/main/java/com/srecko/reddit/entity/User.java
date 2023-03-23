package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type User.
 *
 * @author Srecko Nikolic
 */
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

  @Id
  @GeneratedValue
  private Long id;

  private String firstName;

  private String lastName;

  @NotEmpty
  @Size(min = 10)
  @Email(message = "Email is not valid",
      regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-"
          + "\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c"
          + "\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
          + "\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"
          + "\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08"
          + "\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-"
          + "\\x7f])+)\\])")
  private String email;

  @NotEmpty
  @Size(min = 3, max = 16, message = "is required")
  private String username;

  @NotEmpty
  @Size(min = 6, max = 100)
  private String password;

  private String country;

  private Date registrationDate = new Date();

  @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
  private boolean enabled = false;

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
      orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Post> posts = new ArrayList<>();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
      orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Comment> comments = new ArrayList<>();

  /**
   * Instantiates a new User.
   */
  public User() {
  }

  /**
   * Instantiates a new User.
   *
   * @param firstName the first name
   * @param lastName  the last name
   * @param email     the email
   * @param username  the username
   * @param password  the password
   * @param country   the country
   * @param enabled   the enabled
   */
  public User(String firstName, String lastName, String email, String username, String password,
      String country, boolean enabled) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.country = country;
    this.enabled = enabled;
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
   * Gets first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets first name.
   *
   * @param firstName the first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets last name.
   *
   * @param lastName the last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets email.
   *
   * @param email the email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets country.
   *
   * @param country the country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Gets registration date.
   *
   * @return the registration date
   */
  public Date getRegistrationDate() {
    return registrationDate;
  }

  /**
   * Sets registration date.
   *
   * @param registrationDate the registration date
   */
  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
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

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Is enabled boolean.
   *
   * @return the boolean
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets enabled.
   *
   * @param enabled the enabled
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}