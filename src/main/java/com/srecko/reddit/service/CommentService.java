package com.srecko.reddit.service;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import java.util.List;

/**
 * The interface Comment service.
 *
 * @author Srecko Nikolic
 */
public interface CommentService {

  /**
   * Gets all comments for post.
   *
   * @param postId the post id
   * @return the all comments for post
   */
  List<Comment> getAllCommentsForPost(Long postId);

  /**
   * Gets all comments for username.
   *
   * @param username the username
   * @return the all comments for username
   */
  List<Comment> getAllCommentsForUsername(String username);

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
   * @return the all comments
   */
  List<Comment> getAllComments();
}
