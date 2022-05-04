package com.srecko.reddit.controller;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comments/")
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
    public ResponseEntity<List<Comment>> getCommentsForUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(commentService.getAllCommentsForUsername(username));
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/comments/").toUriString());
        return ResponseEntity.created(uri).body(commentService.save(commentDto));
    }

    @DeleteMapping("{commentId}")
    private ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.delete(commentId));
    }
}
