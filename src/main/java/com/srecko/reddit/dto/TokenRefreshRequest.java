package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;

public class TokenRefreshRequest {

  @NotEmpty
  private String refreshToken;

  public TokenRefreshRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public TokenRefreshRequest() {
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
