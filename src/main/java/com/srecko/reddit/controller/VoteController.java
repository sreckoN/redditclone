package com.srecko.reddit.controller;

import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Vote;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.VoteService;
import jakarta.validation.Valid;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The type Vote controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/votes")
public class VoteController {

  private final VoteService voteService;

  private static final Logger logger = LogManager.getLogger(VoteController.class);

  /**
   * Instantiates a new Vote controller.
   *
   * @param voteService the vote service
   */
  @Autowired
  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  /**
   * Save post vote.
   *
   * @param voteDto       the vote dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping("/post")
  public ResponseEntity<Vote> savePostVote(@Valid @RequestBody VotePostDto voteDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
    Vote savedVote = voteService.savePostVote(voteDto);
    logger.info("Successfully saved vote ({}) for post {}", savedVote.getId(), voteDto.getPostId());
    return ResponseEntity.created(uri).body(savedVote);
  }

  /**
   * Delete post vote.
   *
   * @param voteId the vote id
   * @return the response entity
   */
  @DeleteMapping("/post/{voteId}")
  public ResponseEntity<Vote> deletePostVote(@PathVariable("voteId") Long voteId) {
    Vote deletedVote = voteService.deletePostVote(voteId);
    logger.info("Deleted vote: {}", deletedVote.getId());
    return ResponseEntity.ok(deletedVote);
  }

  /**
   * Save comment vote.
   *
   * @param voteDto       the vote dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping("/comment")
  public ResponseEntity<Vote> saveCommentVote(@Valid @RequestBody VoteCommentDto voteDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
    Vote savedVote = voteService.saveCommentVote(voteDto);
    logger.info("Successfully saved vote ({}) for comment {}",
        savedVote.getId(), voteDto.getCommentId());
    return ResponseEntity.created(uri).body(savedVote);
  }

  /**
   * Delete comment vote.
   *
   * @param voteId the vote id
   * @return the response entity
   */
  @DeleteMapping("/comment/{voteId}")
  public ResponseEntity<Vote> deleteCommentVote(@PathVariable("voteId") Long voteId) {
    Vote deletedVote = voteService.deleteCommentVote(voteId);
    logger.info("Deleted vote: {}", deletedVote.getId());
    return ResponseEntity.ok(deletedVote);
  }
}