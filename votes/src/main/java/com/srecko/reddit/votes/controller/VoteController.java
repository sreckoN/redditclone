package com.srecko.reddit.votes.controller;

import com.srecko.reddit.votes.assembler.VoteCommentModelAssembler;
import com.srecko.reddit.votes.assembler.VotePostModelAssembler;
import com.srecko.reddit.votes.dto.VoteCommentDto;
import com.srecko.reddit.votes.dto.VoteCommentRequest;
import com.srecko.reddit.votes.dto.VotePostDto;
import com.srecko.reddit.votes.dto.VotePostRequest;
import com.srecko.reddit.votes.exception.DtoValidationException;
import com.srecko.reddit.votes.service.VoteService;
import jakarta.validation.Valid;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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
  private final VotePostModelAssembler postModelAssembler;
  private final VoteCommentModelAssembler commentModelAssembler;

  private static final Logger logger = LogManager.getLogger(VoteController.class);

  /**
   * Instantiates a new Vote controller.
   *
   * @param voteService           the vote service
   * @param postModelAssembler    the post model assembler
   * @param commentModelAssembler the comment model assembler
   */
  @Autowired
  public VoteController(VoteService voteService,
      VotePostModelAssembler postModelAssembler,
      VoteCommentModelAssembler commentModelAssembler) {
    this.voteService = voteService;
    this.postModelAssembler = postModelAssembler;
    this.commentModelAssembler = commentModelAssembler;
  }

  /**
   * Save post vote.
   *
   * @param voteDto       the vote dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping("/post")
  public ResponseEntity<EntityModel<VotePostDto>> savePostVote(
      @Valid @RequestBody VotePostRequest voteDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
    VotePostDto savedVote = voteService.savePostVote(voteDto);
    EntityModel<VotePostDto> votePostDtoEntityModel = postModelAssembler.toModel(savedVote);
    logger.info("Successfully saved vote ({}) for post {}", savedVote.getId(), voteDto.getPostId());
    return ResponseEntity.created(uri).body(votePostDtoEntityModel);
  }

  /**
   * Delete post vote.
   *
   * @param voteId the vote id
   * @return the response entity
   */
  @DeleteMapping("/post/{voteId}")
  public ResponseEntity<EntityModel<VotePostDto>> deletePostVote(
      @PathVariable("voteId") Long voteId) {
    VotePostDto deletedVote = voteService.deletePostVote(voteId);
    EntityModel<VotePostDto> votePostDtoEntityModel = postModelAssembler.toModel(deletedVote);
    logger.info("Deleted vote: {}", deletedVote.getId());
    return ResponseEntity.ok(votePostDtoEntityModel);
  }

  /**
   * Save comment vote.
   *
   * @param voteDto       the vote dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping("/comment")
  public ResponseEntity<EntityModel<VoteCommentDto>> saveCommentVote(
      @Valid @RequestBody VoteCommentRequest voteDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
    VoteCommentDto savedVote = voteService.saveCommentVote(voteDto);
    EntityModel<VoteCommentDto> voteCommentDtoEntityModel = commentModelAssembler.toModel(
        savedVote);
    logger.info("Successfully saved vote ({}) for comment {}",
        savedVote.getId(), voteDto.getCommentId());
    return ResponseEntity.created(uri).body(voteCommentDtoEntityModel);
  }

  /**
   * Delete comment vote.
   *
   * @param voteId the vote id
   * @return the response entity
   */
  @DeleteMapping("/comment/{voteId}")
  public ResponseEntity<EntityModel<VoteCommentDto>> deleteCommentVote(
      @PathVariable("voteId") Long voteId) {
    VoteCommentDto deletedVote = voteService.deleteCommentVote(voteId);
    EntityModel<VoteCommentDto> voteCommentDtoEntityModel = commentModelAssembler.toModel(
        deletedVote);
    logger.info("Deleted vote: {}", deletedVote.getId());
    return ResponseEntity.ok(voteCommentDtoEntityModel);
  }
}