package com.srecko.reddit.comments.controller;

import com.srecko.reddit.comments.assembler.CommentModelAssembler;
import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.exception.DtoValidationException;
import com.srecko.reddit.comments.service.CommentService;
import jakarta.validation.Valid;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestMethod;
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
  public ResponseEntity<PagedModel<EntityModel<CommentDto>>> getCommentsForPost(
      @PathVariable("postId") Long postId,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<CommentDto> assembler) {
    Page<CommentDto> page = commentService.getAllCommentsForPost(postId, pageable);
    PagedModel<EntityModel<CommentDto>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments for post with id: {}", postId);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets comments for comment.
   *
   * @param commentId the comment id
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the comments for comment
   */
  @GetMapping("/comment/{commentId}")
  public ResponseEntity<PagedModel<EntityModel<CommentDto>>> getCommentsForComment(
      @PathVariable("commentId") Long commentId,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<CommentDto> assembler) {
    Page<CommentDto> page = commentService.getAllCommentsForComment(commentId, pageable);
    PagedModel<EntityModel<CommentDto>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments for comment with id: {}", commentId);
    return ResponseEntity.ok(pagedModel);
  }

  // todo: get comments for comment

  /**
   * Gets comments for username.
   *
   * @param userId    the user id
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the comments for username
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<PagedModel<EntityModel<CommentDto>>> getCommentsForUser(
      @PathVariable("userId") Long userId,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<CommentDto> assembler) {
    Page<CommentDto> page = commentService.getAllCommentsForUser(userId, pageable);
    PagedModel<EntityModel<CommentDto>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments for user: {}", userId);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets comment.
   *
   * @param commentId the comment id
   * @return the comment
   */
  @GetMapping("/{commentId}")
  public ResponseEntity<EntityModel<CommentDto>> getComment(
      @PathVariable("commentId") Long commentId) {
    CommentDto comment = commentService.getComment(commentId);
    EntityModel<CommentDto> commentDtoEntityModel = commentModelAssembler.toModel(comment);
    logger.info("Returning comment by id: {}", commentId);
    return ResponseEntity.ok(commentDtoEntityModel);
  }

  /**
   * Gets all comments.
   *
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the all comments
   */
  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<CommentDto>>> getAllComments(
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<CommentDto> assembler) {
    Page<CommentDto> page = commentService.getAllComments(pageable);
    PagedModel<EntityModel<CommentDto>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning all comments");
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Create comment.
   *
   * @param commentRequest the comment dto
   * @param bindingResult  the binding result
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<EntityModel<CommentDto>> createComment(
      @Valid @RequestBody CommentRequest commentRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/comments/").toUriString());
    CommentDto comment = commentService.save(commentRequest);
    EntityModel<CommentDto> commentDtoEntityModel = commentModelAssembler.toModel(comment);
    logger.info("Successfully created a new comment: {}", comment.getId());
    return ResponseEntity.created(uri).body(commentDtoEntityModel);
  }

  /**
   * Delete comment.
   *
   * @param commentId the comment id
   * @return the response entity
   */
  @DeleteMapping("{commentId}")
  public ResponseEntity<EntityModel<CommentDto>> deleteComment(
      @PathVariable("commentId") Long commentId) {
    CommentDto deletedComment = commentService.delete(commentId);
    EntityModel<CommentDto> commentDtoEntityModel = commentModelAssembler.toModel(deletedComment);
    logger.info("Deleted the comment with id: {}", commentId);
    return ResponseEntity.ok(commentDtoEntityModel);
  }

  /**
   * Check if exists.
   *
   * @param commentId the comment id
   */
  @RequestMapping(method = RequestMethod.HEAD, value = "/checkIfExists")
  public void checkIfExists(@RequestBody Long commentId) {
    commentService.checkIfExists(commentId);
  }

  /**
   * Search comments paged model.
   *
   * @param query     the query
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the paged model
   */
  @PostMapping("/search")
  public PagedModel<EntityModel<CommentDto>> search(@RequestBody String query,
      @PageableDefault(sort = "name", direction = Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<CommentDto> assembler) {
    Page<CommentDto> page = commentService.search(query, pageable);
    PagedModel<EntityModel<CommentDto>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning a page {}/{} of users", page.getNumber(), page.getTotalPages());
    return pagedModel;
  }
}
