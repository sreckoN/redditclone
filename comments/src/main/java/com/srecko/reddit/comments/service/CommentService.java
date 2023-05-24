package com.srecko.reddit.comments.service;

import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.dto.CommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Comment service.
 *
 * @author Srecko Nikolic
 */
public interface CommentService {

  /**
   * Gets all comments for post.
   *
   * @param postId   the post id
   * @param pageable the pageable
   * @return the all comments for post
   */
  Page<CommentDto> getAllCommentsForPost(Long postId, Pageable pageable);

  /**
   * Gets all comments for comment.
   *
   * @param commentId the comment id
   * @param pageable  the pageable
   * @return the all comments for comment
   */
  Page<CommentDto> getAllCommentsForComment(Long commentId, Pageable pageable);

  /**
   * Gets all comments for username.
   *
   * @param username the username
   * @param pageable the pageable
   * @return the all comments for username
   */
  Page<CommentDto> getAllCommentsForUser(Long username, Pageable pageable);

  /**
   * Save comment.
   *
   * @param commentRequest the comment dto
   * @return the comment
   */
  CommentDto save(CommentRequest commentRequest);

  /**
   * Delete comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  CommentDto delete(Long commentId);

  /**
   * Gets comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  CommentDto getComment(Long commentId);

  /**
   * Gets all comments.
   *
   * @param pageable the pageable
   * @return the all comments
   */
  Page<CommentDto> getAllComments(Pageable pageable);

  /**
   * Check if exists.
   *
   * @param commentId the comment id
   */
  void checkIfExists(Long commentId);

  /**
   * Search page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<CommentDto> search(String query, Pageable pageable);

  /**
   * Increase comment counter.
   *
   * @param parentId the parent id
   */
  void increaseCommentCounter(Long parentId);

  /**
   * Decrease comment counter.
   *
   * @param parentId the parent id
   */
  void decreaseCommentCounter(Long parentId);
}
