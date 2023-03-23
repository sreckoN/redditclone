package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;
import java.util.List;

/**
 * The interface User service.
 *
 * @author Srecko Nikolic
 */
public interface UserService {

  /**
   * Gets users.
   *
   * @return the users
   */
  List<User> getUsers();

  /**
   * Gets user by username.
   *
   * @param username the username
   * @return the user by username
   */
  User getUserByUsername(String username);

  /**
   * Gets user by email.
   *
   * @param email the email
   * @return the user by email
   */
  User getUserByEmail(String email);

  /**
   * Delete user user.
   *
   * @param username the username
   * @return the user
   */
  User deleteUser(String username);

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
  User save(User user);

  /**
   * Delete unverified users.
   */
  void deleteUnverifiedUsers();
}
