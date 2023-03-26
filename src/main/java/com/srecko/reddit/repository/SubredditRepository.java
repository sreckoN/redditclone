package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Subreddit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Subreddit repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

  Optional<Subreddit> findById(Long id);

  /**
   * Find by name containing page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Subreddit> findByNameContainingIgnoreCase(String query, Pageable pageable);
}