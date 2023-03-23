package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
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
   * @param subreddit the subreddit
   * @return the list
   */
  List<Post> findAllBySubreddit(Subreddit subreddit);

  /**
   * Find all by user list.
   *
   * @param user the user
   * @return the list
   */
  List<Post> findAllByUser(User user);

  void deleteById(Long id);

  /**
   * Find by title containing page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Post> findByTitleContaining(String query, Pageable pageable);
}
