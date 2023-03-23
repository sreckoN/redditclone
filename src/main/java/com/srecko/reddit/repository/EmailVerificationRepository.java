package com.srecko.reddit.repository;

import com.srecko.reddit.entity.EmailVerificationToken;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Email verification repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, Long> {

  /**
   * Gets email verification token by token.
   *
   * @param token the token
   * @return the email verification token by token
   */
  Optional<EmailVerificationToken> getEmailVerificationTokenByToken(String token);

  /**
   * Delete by user id.
   *
   * @param userId the user id
   */
  void deleteByUser_Id(Long userId);

  /**
   * Find all by expiry date before list.
   *
   * @param now the now
   * @return the list
   */
  List<EmailVerificationToken> findAllByExpiryDateBefore(Date now);
}
