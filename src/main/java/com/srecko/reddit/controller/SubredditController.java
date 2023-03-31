package com.srecko.reddit.controller;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.SubredditService;
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
 * The type Subreddit controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/subreddits")
public class SubredditController {

  private final SubredditService subredditService;

  private static final Logger logger = LogManager.getLogger(SubredditController.class);

  /**
   * Instantiates a new Subreddit controller.
   *
   * @param subredditService the subreddit service
   */
  @Autowired
  public SubredditController(SubredditService subredditService) {
    this.subredditService = subredditService;
  }

  /**
   * Gets all.
   *
   * @return the all
   */
  @GetMapping
  public ResponseEntity<List<Subreddit>> getAll() {
    List<Subreddit> all = subredditService.getAll();
    logger.info("Returning all subreddits");
    return ResponseEntity.ok(all);
  }

  /**
   * Gets subreddit.
   *
   * @param id the id
   * @return the subreddit
   */
  @GetMapping("/{subredditId}")
  public ResponseEntity<Subreddit> getSubreddit(@PathVariable("subredditId") Long id) {
    Subreddit subreddit = subredditService.getSubredditById(id);
    logger.info("Returning a subreddit with id: {}", id);
    return ResponseEntity.ok(subreddit);
  }

  /**
   * Save subreddit.
   *
   * @param subredditDto  the subreddit dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<Subreddit> save(@Valid @RequestBody SubredditDto subredditDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/subreddits/")
            .toUriString());
    Subreddit saved = subredditService.save(subredditDto);
    logger.info("Successfully created a subreddit: {}", saved.getId());
    return ResponseEntity.created(uri).body(saved);
  }

  /**
   * Delete subreddit.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("/{subredditId}")
  public ResponseEntity<Subreddit> delete(@PathVariable("subredditId") Long id) {
    Subreddit deleted = subredditService.delete(id);
    logger.info("Deleted subreddit with id: {}", deleted.getId());
    return ResponseEntity.ok(deleted);
  }

  /**
   * Update subreddit.
   *
   * @param subredditDto  the subreddit dto
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PutMapping
  public ResponseEntity<Subreddit> update(@Valid @RequestBody SubredditDto subredditDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new DtoValidationException(bindingResult.getAllErrors());
    }
    Subreddit updated = subredditService.update(subredditDto);
    logger.info("Updated subreddit with id: {}", updated.getId());
    return ResponseEntity.ok(updated);
  }
}
