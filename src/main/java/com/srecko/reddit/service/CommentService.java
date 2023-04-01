package com.srecko.reddit.service;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
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
  Page<Comment> getAllCommentsForPost(Long postId, Pageable pageable);

  /**
   * Gets all comments for username.
   *
   * @param username the username
   * @param pageable the pageable
   * @return the all comments for username
   */
  Page<Comment> getAllCommentsForUsername(String username, Pageable pageable);

  /**
   * Save comment.
   *
   * @param commentDto the comment dto
   * @return the comment
   */
  Comment save(CommentDto commentDto);

  /**
   * Delete comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  Comment delete(Long commentId);

  /**
   * Gets comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  Comment getComment(Long commentId);

  /**
   * Gets all comments.
   *
   * @param pageable the pageable
   * @return the all comments
   */
  Page<Comment> getAllComments(Pageable pageable);
}
