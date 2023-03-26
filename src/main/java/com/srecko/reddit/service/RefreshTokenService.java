package com.srecko.reddit.service;

import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.TokenRefreshRequest;
import com.srecko.reddit.entity.User;

/**
 * The interface Refresh token service.
 *
 * @author Srecko Nikolic
 */
public interface RefreshTokenService {

  /**
   * Gets new access token.
   *
   * @param tokenRefreshRequest the token refresh request
   * @return the new access token
   */
  AuthenticationResponse getNewAccessToken(TokenRefreshRequest tokenRefreshRequest);

  /**
   * Save refresh token.
   *
   * @param token the token
   * @param user  the user
   */
  void saveRefreshToken(String token, User user);

  /**
   * Save refresh token.
   *
   * @param token    the token
   * @param username the username
   */
  void saveRefreshToken(String token, String username);

  /**
   * Delete refresh token.
   *
   * @param user the user
   */
  void deleteRefreshToken(User user);

  /**
   * Remove expired tokens.
   */
  void removeExpiredTokens();
}