package com.srecko.reddit.service;

import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.TokenRefreshRequest;
import com.srecko.reddit.entity.RefreshToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.authentication.RefreshTokenInvalidException;
import com.srecko.reddit.exception.authentication.RefreshTokenNotFoundException;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Refresh token service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtils jwtUtils;
  private final UserService userService;

  private static final Logger logger = LogManager.getLogger(RefreshTokenServiceImpl.class);

  /**
   * Instantiates a new Refresh token service.
   *
   * @param refreshTokenRepository the refresh token repository
   * @param jwtUtils               the jwt utils
   * @param userService            the user service
   */
  @Autowired
  public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils,
      UserService userService) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtUtils = jwtUtils;
    this.userService = userService;
  }

  @Override
  public AuthenticationResponse getNewAccessToken(TokenRefreshRequest tokenRefreshRequest) {
    logger.info("Getting new access token for user");
    String refreshToken = tokenRefreshRequest.getRefreshToken();
    Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken);
    if (token.isEmpty()) {
      throw new RefreshTokenNotFoundException();
    }
    User user = userService.getUserByEmail(token.get().getUser().getEmail());
    if (!jwtUtils.isTokenValid(refreshToken, user.getUsername())) {
      throw new RefreshTokenInvalidException();
    }
    String accessToken = jwtUtils.getAccessToken(user.getEmail());
    return new AuthenticationResponse(user.getUsername(), accessToken, refreshToken);
  }

  @Override
  public void saveRefreshToken(String token, User user) {
    logger.info("Saving refresh token to the database for user: {}", user.getUsername());
    Date expirationDate = jwtUtils.extractExpiration(token);
    RefreshToken refreshToken = new RefreshToken(user, token, expirationDate.toInstant());
    refreshTokenRepository.save(refreshToken);
  }

  @Override
  public void saveRefreshToken(String token, String username) {
    logger.info("Saving refresh token to the database for user: {}", username);
    User user = userService.getUserByUsername(username);
    saveRefreshToken(token, user);
  }

  @Override
  public void deleteRefreshToken(User user) {
    logger.info("Deleting refresh token to the database for user: {}", user.getUsername());
    refreshTokenRepository.deleteByUser(user);
  }

  @Override
  public void removeExpiredTokens() {
    List<RefreshToken> expiredTokens = refreshTokenRepository.findAllByExpiryDateBefore(
        Instant.now());
    refreshTokenRepository.deleteAll(expiredTokens);
  }
}