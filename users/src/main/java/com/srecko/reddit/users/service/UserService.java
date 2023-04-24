package com.srecko.reddit.users.service;

import com.srecko.reddit.users.dto.UserDto;
import com.srecko.reddit.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User service.
 *
 * @author Srecko Nikolic
 */
public interface UserService {

  /**
   * Gets users.
   *
   * @param pageable the pageable
   * @return the users
   */
  Page<UserDto> getUsers(Pageable pageable);

  /**
   * Gets user by username.
   *
   * @param username the username
   * @return the user by username
   */
  UserDto getUserByUsername(String username);

  /**
   * Gets user by username.
   *
   * @param username the username
   * @return the user by username
   */
  User getUserByUsernameInternal(String username);

  /**
   * Gets user by email internal.
   *
   * @param email the email
   * @return the user by email internal
   */
  User getUserByEmail(String email);

  /**
   * Delete user user.
   *
   * @param username the username
   * @return the user
   */
  UserDto deleteUser(String username);

  /**
   * Exists user by email boolean.
   *
   * @param email the email
   * @return the boolean
   */
  boolean existsUserByEmail(String email);

  /**
   * Exists user by username boolean.
   *
   * @param username the username
   * @return the boolean
   */
  boolean existsUserByUsername(String username);

  /**
   * Save user.
   *
   * @param user the user
   * @return the user
   */
  UserDto save(User user);

  /**
   * Delete unverified users.
   */
  void deleteUnverifiedUsers();

  /**
   * Gets user id by username.
   *
   * @param username the username
   * @return the user id by username
   */
  Long getUserIdByUsername(String username);
}
