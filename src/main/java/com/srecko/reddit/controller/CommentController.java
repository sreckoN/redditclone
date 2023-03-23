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

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping("/post/{postId}")
  public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable("postId") Long postId) {
    return ResponseEntity.ok(commentService.getAllCommentsForPost(postId));
  }

  @GetMapping("/user/{username}")
  public ResponseEntity<List<Comment>> getCommentsForUsername(
      @PathVariable("username") String username) {
    return ResponseEntity.ok(commentService.getAllCommentsForUsername(username));
  }

  @GetMapping("/{commentId}")
  public ResponseEntity<Comment> getComment(@PathVariable("commentId") Long commentId) {
    return ResponseEntity.ok(commentService.getComment(commentId));
  }

  @GetMapping
  public ResponseEntity<List<Comment>> getAllComments() {
    return ResponseEntity.ok(commentService.getAllComments());
  }

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

  @DeleteMapping("{commentId}")
  private ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId) {
    return ResponseEntity.ok(commentService.delete(commentId));
  }
}
