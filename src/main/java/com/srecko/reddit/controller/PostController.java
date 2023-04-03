package com.srecko.reddit.controller;

import com.srecko.reddit.assembler.PostModelAssembler;
import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.requests.CreatePostRequest;
import com.srecko.reddit.dto.requests.UpdatePostRequest;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.PostService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The type Post controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;
  private final PostModelAssembler postModelAssembler;

  private static final Logger logger = LogManager.getLogger(PostController.class);

  /**
   * Instantiates a new Post controller.
   *
   * @param postService        the post service
   * @param postModelAssembler the post model assembler
   */
  @Autowired
  public PostController(PostService postService,
      PostModelAssembler postModelAssembler) {
    this.postService = postService;
    this.postModelAssembler = postModelAssembler;
  }

  /**
   * Gets all posts.
   *
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the all posts
   */
  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> getAllPosts(
      @PageableDefault(sort = "dateOfCreation", direction = Direction.ASC)Pageable pageable,
      PagedResourcesAssembler<PostDto> assembler) {
    Page<PostDto> page = postService.getAllPosts(pageable);
    PagedModel<EntityModel<PostDto>> pagedModel = assembler.toModel(page, postModelAssembler);
    logger.info("Returning all posts");
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets post.
   *
   * @param postId the post id
   * @return the post
   */
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> getPost(@PathVariable("postId") Long postId) {
    PostDto post = postService.getPost(postId);
    logger.info("Returning a post with id: {}", post.getId());
    return ResponseEntity.ok(post);
  }

  /**
   * Gets all posts for subreddit.
   *
   * @param subredditId the subreddit id
   * @param pageable    the pageable
   * @param assembler   the assembler
   * @return the all posts for subreddit
   */
  @GetMapping("/subreddit/{subredditId}")
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> getAllPostsForSubreddit(
      @PathVariable("subredditId") Long subredditId,
      @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<PostDto> assembler) {
    Page<PostDto> page = postService.getAllPostsForSubreddit(subredditId, pageable);
    PagedModel<EntityModel<PostDto>> pagedModel = assembler.toModel(page, postModelAssembler);
    logger.info("Returning posts for subreddit: {}", subredditId);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets posts for user.
   *
   * @param username  the username
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the posts for user
   */
  @GetMapping("/user/{username}")
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> getPostsForUser(
      @PathVariable("username") String username,
      @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<PostDto> assembler) {
    Page<PostDto> page = postService.getAllPostsForUser(username, pageable);
    PagedModel<EntityModel<PostDto>> pagedModel = assembler.toModel(page, postModelAssembler);
    logger.info("Returning posts for user: {}", username);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Create post.
   *
   * @param postRequest   the post request
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<PostDto> create(@Valid @RequestBody CreatePostRequest postRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts").toUriString());
    PostDto savedPost = postService.save(postRequest);
    logger.info("Successfully created a new post: {}", savedPost.getId());
    return ResponseEntity.created(uri).body(savedPost);
  }

  /**
   * Delete post.
   *
   * @param postId the post id
   * @return the response entity
   */
  @DeleteMapping("/{postId}")
  public ResponseEntity<PostDto> delete(@PathVariable("postId") Long postId) {
    PostDto deletedPost = postService.delete(postId);
    logger.info("Deleted a post with id: {}", postId);
    return ResponseEntity.ok(deletedPost);
  }

  /**
   * Update post.
   *
   * @param updatePostRequest the post dto
   * @param bindingResult     the binding result
   * @return the response entity
   */
  @PutMapping
  public ResponseEntity<PostDto> update(@Valid @RequestBody UpdatePostRequest updatePostRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    PostDto updatedPost = postService.update(updatePostRequest);
    logger.info("Updated post: {}", updatedPost.getId());
    return ResponseEntity.ok(updatedPost);
  }
}