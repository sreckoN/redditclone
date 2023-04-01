package com.srecko.reddit.controller;

import com.srecko.reddit.assembler.UserModelAssembler;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
  private final UserModelAssembler userModelAssembler;

  private static final Logger logger = LogManager.getLogger(UserController.class);

  /**
   * Instantiates a new User controller.
   *
   * @param userService        the user service
   * @param userModelAssembler the user model assembler
   */
  @Autowired
  public UserController(UserService userService,
      UserModelAssembler userModelAssembler) {
    this.userService = userService;
    this.userModelAssembler = userModelAssembler;
  }

  /**
   * Gets users.
   *
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the users
   */
  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<User>>> getUsers(
      @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<User> assembler) {
    Page<User> page = userService.getUsers(pageable);
    PagedModel<EntityModel<User>> pagedModel = assembler.toModel(page, userModelAssembler);
    logger.info("Returning all users");
    return ResponseEntity.ok().body(pagedModel);
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