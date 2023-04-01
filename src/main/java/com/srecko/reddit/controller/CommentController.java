package com.srecko.reddit.controller;

import com.srecko.reddit.assembler.CommentModelAssembler;
import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.CommentService;
import jakarta.validation.Valid;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
  private final CommentModelAssembler commentModelAssembler;

  private static final Logger logger = LogManager.getLogger(CommentController.class);

  /**
   * Instantiates a new Comment controller.
   *
   * @param commentService        the comment service
   * @param commentModelAssembler the comment model assembler
   */
  @Autowired
  public CommentController(CommentService commentService,
      CommentModelAssembler commentModelAssembler) {
    this.commentService = commentService;
    this.commentModelAssembler = commentModelAssembler;
  }

  /**
   * Gets comments for post.
   *
   * @param postId    the post id
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the comments for post
   */
  @GetMapping("/post/{postId}")
  public ResponseEntity<PagedModel<EntityModel<Comment>>> getCommentsForPost(
      @PathVariable("postId") Long postId,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<Comment> assembler) {
    Page<Comment> page = commentService.getAllCommentsForPost(postId, pageable);
    PagedModel<EntityModel<Comment>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments for post with id: {}", postId);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets comments for username.
   *
   * @param username  the username
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the comments for username
   */
  @GetMapping("/user/{username}")
  public ResponseEntity<PagedModel<EntityModel<Comment>>> getCommentsForUsername(
      @PathVariable("username") String username,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<Comment> assembler) {
    Page<Comment> page = commentService.getAllCommentsForUsername(username, pageable);
    PagedModel<EntityModel<Comment>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments for username: {}", username);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  @GetMapping("/{commentId}")
  public ResponseEntity<Comment> getComment(@PathVariable("commentId") Long commentId) {
    Comment comment = commentService.getComment(commentId);
    logger.info("Returning comment by id: {}", commentId);
    return ResponseEntity.ok(comment);
  }

  /**
   * Gets all comments.
   *
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the all comments
   */
  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<Comment>>> getAllComments(
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<Comment> assembler) {
    Page<Comment> page = commentService.getAllComments(pageable);
    PagedModel<EntityModel<Comment>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments");
    return ResponseEntity.ok(pagedModel);
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
    logger.info("Successfully created a new comment: {}", comment.getId());
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
    Comment deletedComment = commentService.delete(commentId);
    logger.info("Deleted the comment with id: {}", commentId);
    return ResponseEntity.ok(deletedComment);
  }
}
