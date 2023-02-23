package com.srecko.reddit.controller;

import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Vote;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/post")
    public ResponseEntity<Vote> savePostVote(@Valid @RequestBody VotePostDto voteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new DtoValidationException(bindingResult.getAllErrors());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
        return ResponseEntity.created(uri).body(voteService.savePostVote(voteDto));
    }

    @DeleteMapping("/post/{voteId}")
    public ResponseEntity<Vote> deletePostVote(@PathVariable("voteId") Long voteId) {
        return ResponseEntity.ok(voteService.deletePostVote(voteId));
    }

    @PostMapping("/comment")
    public ResponseEntity<Vote> saveCommentVote(@Valid @RequestBody VoteCommentDto voteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new DtoValidationException(bindingResult.getAllErrors());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
        return ResponseEntity.created(uri).body(voteService.saveCommentVote(voteDto));
    }

    @DeleteMapping("/comment/{voteId}")
    public ResponseEntity<Vote> deleteCommentVote(@PathVariable("voteId") Long voteId) {
        return ResponseEntity.ok(voteService.deleteCommentVote(voteId));
    }
}