package com.srecko.reddit.scheduler;

import com.srecko.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserUnenabledAccountRemoverScheduler {

    private UserService userService;

    @Autowired
    public UserUnenabledAccountRemoverScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 3600000)
    public void deleteUnverifiedUsers() {
        userService.deleteUnverifiedUsers();
    }
}
