package com.srecko.reddit.controller;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.PostService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping
  public ResponseEntity<List<Post>> getAllPosts() {
    return ResponseEntity.ok(postService.getAllPosts());
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Post> getPost(@PathVariable("postId") Long postId) {
    return ResponseEntity.ok(postService.getPost(postId));
  }

  @GetMapping("/subreddit/{subredditId}")
  public ResponseEntity<List<Post>> getAllPostsForSubreddit(
      @PathVariable("subredditId") Long subredditId) {
    return ResponseEntity.ok(postService.getAllPostsForSubreddit(subredditId));
  }

  @GetMapping("/user/{username}")
  public ResponseEntity<List<Post>> getPostsForUser(@PathVariable("username") String username) {
    return ResponseEntity.ok(postService.getAllPostsForUser(username));
  }

  @PostMapping
  public ResponseEntity<Post> create(@Valid @RequestBody CreatePostDto postRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts").toUriString());
    return ResponseEntity.created(uri).body(postService.save(postRequest));
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Post> delete(@PathVariable("postId") Long postId) {
    return ResponseEntity.ok(postService.delete(postId));
  }

  @PutMapping
  public ResponseEntity<Post> update(@Valid @RequestBody UpdatePostDto postDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    return ResponseEntity.ok(postService.update(postDto));
  }
}