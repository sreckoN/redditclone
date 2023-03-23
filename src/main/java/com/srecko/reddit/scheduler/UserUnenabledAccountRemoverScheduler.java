package com.srecko.reddit.scheduler;

import com.srecko.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The type User unenabled account remover scheduler.
 *
 * @author Srecko Nikolic
 */
@Component
public class UserUnenabledAccountRemoverScheduler {

  private UserService userService;

  /**
   * Instantiates a new User unenabled account remover scheduler.
   *
   * @param userService the user service
   */
  @Autowired
  public UserUnenabledAccountRemoverScheduler(UserService userService) {
    this.userService = userService;
  }

  /**
   * Delete unverified users.
   */
  @Scheduled(fixedDelay = 3600000)
  public void deleteUnverifiedUsers() {
    userService.deleteUnverifiedUsers();
  }
}
