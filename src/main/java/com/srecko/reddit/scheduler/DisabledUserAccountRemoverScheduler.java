package com.srecko.reddit.scheduler;

import com.srecko.reddit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The type User disabled account remover scheduler.
 *
 * @author Srecko Nikolic
 */
@Component
public class DisabledUserAccountRemoverScheduler {

  private final UserService userService;

  private static final Logger logger = LogManager
      .getLogger(DisabledUserAccountRemoverScheduler.class);

  /**
   * Instantiates a new User unenabled account remover scheduler.
   *
   * @param userService the user service
   */
  @Autowired
  public DisabledUserAccountRemoverScheduler(UserService userService) {
    this.userService = userService;
  }

  /**
   * Delete unverified users.
   */
  @Scheduled(fixedDelay = 3600000)
  public void deleteUnverifiedUsers() {
    logger.info("Deleting expired user accounts from database");
    userService.deleteUnverifiedUsers();
  }
}
