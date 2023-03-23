package com.srecko.reddit.repository;

import com.srecko.reddit.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find user by username optional.
   *
   * @param username the username
   * @return the optional
   */
  Optional<User> findUserByUsername(String username);

  /**
   * Find user by email optional.
   *
   * @param email the email
   * @return the optional
   */
  Optional<User> findUserByEmail(String email);

  /**
   * Exists user by username boolean.
   *
   * @param username the username
   * @return the boolean
   */
  boolean existsUserByUsername(String username);

  /**
   * Exists user by email boolean.
   *
   * @param email the email
   * @return the boolean
   */
  boolean existsUserByEmail(String email);

  /**
   * Delete all by enabled false.
   */
  void deleteAllByEnabledFalse();

  /**
   * Find by username containing page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<User> findByUsernameContaining(String query, Pageable pageable);
}