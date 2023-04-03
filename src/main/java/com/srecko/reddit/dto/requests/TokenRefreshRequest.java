package com.srecko.reddit.dto.requests;

import jakarta.validation.constraints.NotEmpty;

/**
 * The type Token refresh request.
 *
 * @author Srecko Nikolic
 */
public class TokenRefreshRequest {

  @NotEmpty
  private String refreshToken;

  /**
   * Instantiates a new Token refresh request.
   *
   * @param refreshToken the refresh token
   */
  public TokenRefreshRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Instantiates a new Token refresh request.
   */
  public TokenRefreshRequest() {
  }

  /**
   * Sets refresh token.
   *
   * @param refreshToken the refresh token
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Gets refresh token.
   *
   * @return the refresh token
   */
  public String getRefreshToken() {
    return refreshToken;
  }
}
