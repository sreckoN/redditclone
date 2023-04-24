package com.srecko.reddit.subreddits.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.subreddits.assembler.SubredditModelAssembler;
import com.srecko.reddit.subreddits.dto.SubredditDto;
import com.srecko.reddit.subreddits.dto.SubredditRequest;
import com.srecko.reddit.subreddits.exception.DtoValidationException;
import com.srecko.reddit.subreddits.service.SubredditService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The type Subreddit controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/subreddits")
public class SubredditController {

  private final SubredditService subredditService;
  private final SubredditModelAssembler subredditModelAssembler;

  private static final Logger logger = LogManager.getLogger(SubredditController.class);

  /**
   * Instantiates a new Subreddit controller.
   *
   * @param subredditService        the subreddit service
   * @param subredditModelAssembler the subreddit model assembler
   */
  @Autowired
  public SubredditController(SubredditService subredditService,
      SubredditModelAssembler subredditModelAssembler) {
    this.subredditService = subredditService;
    this.subredditModelAssembler = subredditModelAssembler;
  }

  /**
   * Gets all.
   *
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the all
   */
  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<SubredditDto>>> getAll(
      @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<SubredditDto> assembler) {
    Page<SubredditDto> page = subredditService.getAll(pageable);
    PagedModel<EntityModel<SubredditDto>> pagedModel = assembler.toModel(page,
        subredditModelAssembler);
    logger.info("Returning page of subreddits");
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Gets subreddit.
   *
   * @param id the id
   * @return the subreddit
   */
  @GetMapping("/{subredditId}")
  public ResponseEntity<EntityModel<SubredditDto>> getSubreddit(
      @PathVariable("subredditId") Long id) {
    SubredditDto subreddit = subredditService.getSubredditById(id);
    EntityModel<SubredditDto> subredditDtoEntityModel = subredditModelAssembler.toModel(subreddit);
    logger.info("Returning a subreddit with id: {}", id);
    return ResponseEntity.ok(subredditDtoEntityModel);
  }

  /**
   * Save subreddit.
   *
   * @param subredditRequest the subreddit dto
   * @param bindingResult    the binding result
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<EntityModel<SubredditDto>> save(
      @Valid @RequestBody SubredditRequest subredditRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/subreddits/")
            .toUriString());
    SubredditDto saved = subredditService.save(subredditRequest);
    EntityModel<SubredditDto> subredditDtoEntityModel = subredditModelAssembler.toModel(saved);
    logger.info("Successfully created a subreddit: {}", saved.getId());
    return ResponseEntity.created(uri).body(subredditDtoEntityModel);
  }

  /**
   * Delete subreddit.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("/{subredditId}")
  public ResponseEntity<SubredditDto> delete(@PathVariable("subredditId") Long id) {
    SubredditDto deleted = subredditService.delete(id);
    EntityModel.of(deleted,
        linkTo(methodOn(SubredditController.class).getSubreddit(deleted.getId())).withSelfRel(),
        linkTo(methodOn(SubredditController.class).getAll(null, null)).withRel("subreddits"));
    logger.info("Deleted subreddit with id: {}", deleted.getId());
    return ResponseEntity.ok(deleted);
  }

  /**
   * Update subreddit.
   *
   * @param subredditRequest the subreddit dto
   * @param bindingResult    the binding result
   * @return the response entity
   */
  @PutMapping
  public ResponseEntity<EntityModel<SubredditDto>> update(
      @Valid @RequestBody SubredditRequest subredditRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    SubredditDto updated = subredditService.update(subredditRequest);
    EntityModel<SubredditDto> subredditDtoEntityModel = subredditModelAssembler.toModel(updated);
    logger.info("Updated subreddit with id: {}", updated.getId());
    return ResponseEntity.ok(subredditDtoEntityModel);
  }

  @RequestMapping(method = RequestMethod.HEAD, value = "/checkIfSubredditExists")
  public void checkIfSubredditExists(@RequestBody Long subredditId) {
    subredditService.checkIfExists(subredditId);
  }
}
