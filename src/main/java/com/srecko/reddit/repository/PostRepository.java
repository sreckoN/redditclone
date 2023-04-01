package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Post repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findById(Long id);

  /**
   * Find all by subreddit list.
   *
   * @param subreddit   the subreddit
   * @param pageRequest the page request
   * @return the list
   */
  Page<Post> findAllBySubreddit(Subreddit subreddit,
      PageRequest pageRequest);

  /**
   * Find all by user list.
   *
   * @param user     the user
   * @param pageable the pageable
   * @return the list
   */
  Page<Post> findAllByUser(User user, Pageable pageable);

  void deleteById(Long id);

  /**
   * Find by title containing page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Post> findByTitleContainingIgnoreCase(String query, Pageable pageable);

  /**
   * Find by subreddit id and title containing ignore case page.
   *
   * @param subredditId the subreddit id
   * @param query       the query
   * @param pageable    the pageable
   * @return the page
   */
  Page<Post> findBySubredditIdAndTitleContainingIgnoreCase(Long subredditId, String query,
      Pageable pageable);
}
