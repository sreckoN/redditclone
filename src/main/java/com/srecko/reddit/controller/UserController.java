package com.srecko.reddit.controller;

import com.srecko.reddit.entity.User;
import com.srecko.reddit.service.UserService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type User controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  private static final Logger logger = LogManager.getLogger(UserController.class);

  /**
   * Instantiates a new User controller.
   *
   * @param userService the user service
   */
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Gets users.
   *
   * @return the users
   */
  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.getUsers();
    logger.info("Returning all users");
    return ResponseEntity.ok().body(users);
  }

  /**
   * Gets user.
   *
   * @param username the username
   * @return the user
   */
  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable("username") String username) {
    User user = userService.getUserByUsername(username);
    logger.info("Returning user with id: {}", user.getId());
    return ResponseEntity.ok().body(user);
  }

  /**
   * Delete user.
   *
   * @param username the username
   * @return the response entity
   */
  @DeleteMapping("/{username}")
  public ResponseEntity<User> delete(@PathVariable("username") String username) {
    User deleted = userService.deleteUser(username);
    logger.info("Deleted user with id: {}", deleted.getId());
    return ResponseEntity.ok().body(deleted);
  }
}