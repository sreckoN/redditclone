package com.srecko.reddit.repository;

import com.srecko.reddit.entity.RefreshToken;
import com.srecko.reddit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  void deleteByUser(User user);

  Optional<RefreshToken> findByToken(String token);

  List<RefreshToken> findAllByExpiryDateBefore(Instant now);
}