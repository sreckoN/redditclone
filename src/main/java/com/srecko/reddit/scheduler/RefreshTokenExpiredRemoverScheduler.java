package com.srecko.reddit.scheduler;

import com.srecko.reddit.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenExpiredRemoverScheduler {

    private RefreshTokenService refreshTokenService;

    @Autowired
    public RefreshTokenExpiredRemoverScheduler(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Scheduled(fixedDelay = 3600000)
    public void deleteExpiredRefreshTokens() {
        refreshTokenService.removeExpiredTokens();
    }
}
