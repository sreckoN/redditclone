package com.srecko.reddit.service;

import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.TokenRefreshRequest;
import com.srecko.reddit.entity.User;

public interface RefreshTokenService {

    AuthenticationResponse getNewAccessToken(TokenRefreshRequest tokenRefreshRequest);
    void saveRefreshToken(String token, User user);
    void deleteRefreshToken(User user);
    void removeExpiredTokens();
}