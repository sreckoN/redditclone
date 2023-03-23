package com.srecko.reddit.dto;

public class AuthenticationResponse {

  private String username;
  private String accessToken;
  private String refreshToken;

  public AuthenticationResponse(String username, String accessToken, String refreshToken) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public AuthenticationResponse() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
