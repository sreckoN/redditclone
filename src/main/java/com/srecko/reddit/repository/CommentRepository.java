package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Comment repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * Find all by post list.
   *
   * @param post     the post
   * @param pageable the pageable
   * @return the list
   */
  Page<Comment> findAllByPost(Post post, Pageable pageable);

  /**
   * Find all by user list.
   *
   * @param user     the user
   * @param pageable the pageable
   * @return the list
   */
  Page<Comment> findAllByUser(User user, Pageable pageable);

  /**
   * Find by text containing page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Comment> findByTextContainingIgnoreCase(String query, Pageable pageable);
}
