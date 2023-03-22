package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Subreddit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findById(Long id);
    Page<Subreddit> findByNameContaining(String query, Pageable pageable);
}