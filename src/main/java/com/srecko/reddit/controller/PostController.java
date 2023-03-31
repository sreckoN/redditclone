package com.srecko.reddit.controller;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

  private static final Logger logger = LogManager.getLogger(PostController.class);

  /**
   * Instantiates a new Post controller.
   *
   * @param postService the post service
   */
  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  /**
   * Gets all posts.
   *
   * @return the all posts
   */
  @GetMapping
  public ResponseEntity<List<Post>> getAllPosts() {
    List<Post> allPosts = postService.getAllPosts();
    logger.info("Returning all posts");
    return ResponseEntity.ok(allPosts);
  }

  /**
   * Gets post.
   *
   * @param postId the post id
   * @return the post
   */
  @GetMapping("/{postId}")
  public ResponseEntity<Post> getPost(@PathVariable("postId") Long postId) {
    Post post = postService.getPost(postId);
    logger.info("Returning a post with id: {}", post.getId());
    return ResponseEntity.ok(post);
  }

  /**
   * Gets all posts for subreddit.
   *
   * @param subredditId the subreddit id
   * @return the all posts for subreddit
   */
  @GetMapping("/subreddit/{subredditId}")
  public ResponseEntity<List<Post>> getAllPostsForSubreddit(
      @PathVariable("subredditId") Long subredditId) {
    List<Post> allPostsForSubreddit = postService.getAllPostsForSubreddit(subredditId);
    logger.info("Returning posts for subreddit: {}", subredditId);
    return ResponseEntity.ok(allPostsForSubreddit);
  }

  /**
   * Gets posts for user.
   *
   * @param username the username
   * @return the posts for user
   */
  @GetMapping("/user/{username}")
  public ResponseEntity<List<Post>> getPostsForUser(@PathVariable("username") String username) {
    List<Post> allPostsForUser = postService.getAllPostsForUser(username);
    logger.info("Returning posts for user: {}", username);
    return ResponseEntity.ok(allPostsForUser);
  }

  /**
   * Create post.
   *
   * @param postRequest   the post request
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<Post> create(@Valid @RequestBody CreatePostDto postRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts").toUriString());
    Post savedPost = postService.save(postRequest);
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
  public ResponseEntity<Post> delete(@PathVariable("postId") Long postId) {
    Post deletedPost = postService.delete(postId);
    logger.info("Deleted a post with id: {}", postId);
    return ResponseEntity.ok(deletedPost);
  }

  /**
   * Update post.
   *
   * @param postDto       the post dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PutMapping
  public ResponseEntity<Post> update(@Valid @RequestBody UpdatePostDto postDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    Post updatedPost = postService.update(postDto);
    logger.info("Updated post: {}", updatedPost.getId());
    return ResponseEntity.ok(updatedPost);
  }
}