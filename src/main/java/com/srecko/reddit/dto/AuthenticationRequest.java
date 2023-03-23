package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * The type Authentication request.
 *
 * @author Srecko Nikolic
 */
public class AuthenticationRequest {

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  /**
   * Instantiates a new Authentication request.
   *
   * @param username the username
   * @param password the password
   */
  public AuthenticationRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Instantiates a new Authentication request.
   */
  public AuthenticationRequest() {
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
}
