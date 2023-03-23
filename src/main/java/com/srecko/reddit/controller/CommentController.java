package com.srecko.reddit.controller;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.CommentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The type Comment controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;

  /**
   * Instantiates a new Comment controller.
   *
   * @param commentService the comment service
   */
  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  /**
   * Gets comments for post.
   *
   * @param postId the post id
   * @return the comments for post
   */
  @GetMapping("/post/{postId}")
  public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable("postId") Long postId) {
    return ResponseEntity.ok(commentService.getAllCommentsForPost(postId));
  }

  /**
   * Gets comments for username.
   *
   * @param username the username
   * @return the comments for username
   */
  @GetMapping("/user/{username}")
  public ResponseEntity<List<Comment>> getCommentsForUsername(
      @PathVariable("username") String username) {
    return ResponseEntity.ok(commentService.getAllCommentsForUsername(username));
  }

  /**
   * Gets comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  @GetMapping("/{commentId}")
  public ResponseEntity<Comment> getComment(@PathVariable("commentId") Long commentId) {
    return ResponseEntity.ok(commentService.getComment(commentId));
  }

  /**
   * Gets all comments.
   *
   * @return the all comments
   */
  @GetMapping
  public ResponseEntity<List<Comment>> getAllComments() {
    return ResponseEntity.ok(commentService.getAllComments());
  }

  /**
   * Create comment.
   *
   * @param commentDto    the comment dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentDto commentDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/comments/").toUriString());
    Comment comment = commentService.save(commentDto);
    return ResponseEntity.created(uri).body(comment);
  }

  /**
   * Delete comment.
   *
   * @param commentId the comment id
   * @return the response entity
   */
  @DeleteMapping("{commentId}")
  public ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId) {
    return ResponseEntity.ok(commentService.delete(commentId));
  }
}
