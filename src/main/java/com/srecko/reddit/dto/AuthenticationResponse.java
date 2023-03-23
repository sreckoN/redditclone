package com.srecko.reddit.dto;

/**
 * The type Authentication response.
 *
 * @author Srecko Nikolic
 */
public class AuthenticationResponse {

  private String username;

  private String accessToken;

  private String refreshToken;

  /**
   * Instantiates a new Authentication response.
   *
   * @param username     the username
   * @param accessToken  the access token
   * @param refreshToken the refresh token
   */
  public AuthenticationResponse(String username, String accessToken, String refreshToken) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  /**
   * Instantiates a new Authentication response.
   */
  public AuthenticationResponse() {
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
   * Gets access token.
   *
   * @return the access token
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Sets access token.
   *
   * @param accessToken the access token
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Gets refresh token.
   *
   * @return the refresh token
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Sets refresh token.
   *
   * @param refreshToken the refresh token
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
