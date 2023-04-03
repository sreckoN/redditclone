package com.srecko.reddit.dto.requests;

import jakarta.validation.constraints.NotEmpty;

/**
 * The type Registration request.
 *
 * @author Srecko Nikolic
 */
public class RegistrationRequest {

  @NotEmpty
  private String firstName;

  private String lastName;

  @NotEmpty
  private String email;

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  private String country;

  /**
   * Instantiates a new Registration request.
   *
   * @param firstName the first name
   * @param lastName  the last name
   * @param email     the email
   * @param username  the username
   * @param password  the password
   * @param country   the country
   */
  public RegistrationRequest(String firstName, String lastName, String email, String username,
      String password, String country) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.country = country;
  }

  /**
   * Instantiates a new Registration request.
   */
  public RegistrationRequest() {
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
}
