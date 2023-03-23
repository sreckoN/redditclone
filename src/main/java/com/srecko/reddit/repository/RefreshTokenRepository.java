package com.srecko.reddit.repository;

import com.srecko.reddit.entity.RefreshToken;
import com.srecko.reddit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Refresh token repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  /**
   * Delete by user.
   *
   * @param user the user
   */
  void deleteByUser(User user);

  /**
   * Find by token optional.
   *
   * @param token the token
   * @return the optional
   */
  Optional<RefreshToken> findByToken(String token);

  /**
   * Find all by expiry date before list.
   *
   * @param now the now
   * @return the list
   */
  List<RefreshToken> findAllByExpiryDateBefore(Instant now);
}