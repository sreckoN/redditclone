package com.srecko.reddit.repository;

import com.srecko.reddit.entity.EmailVerificationToken;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, Long> {

  Optional<EmailVerificationToken> getEmailVerificationTokenByToken(String token);

  void deleteByUser_Id(Long userId);

  List<EmailVerificationToken> findAllByExpiryDateBefore(Date now);
}
