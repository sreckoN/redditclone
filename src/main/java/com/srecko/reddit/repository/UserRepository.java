package com.srecko.reddit.repository;

import com.srecko.reddit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    void deleteAllByEnabledFalse();
    Page<User> findByUsernameContaining(String query, Pageable pageable);
}