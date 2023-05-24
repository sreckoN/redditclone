package com.srecko.reddit.comments.repository;

import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.entity.CommentParentType;
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
   * @param parentType the parent type
   * @param parentId   the parent id
   * @param pageable   the pageable
   * @return the list
   */
  Page<Comment> findAllByParentTypeAndParentId(CommentParentType parentType,
      Long parentId, Pageable pageable);

  /**
   * Find all by user list.
   *
   * @param userId   the user id
   * @param pageable the pageable
   * @return the list
   */
  Page<Comment> findAllByUserId(Long userId, Pageable pageable);

  /**
   * Find by text containing page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Comment> findByTextContainingIgnoreCase(String query, Pageable pageable);
}
