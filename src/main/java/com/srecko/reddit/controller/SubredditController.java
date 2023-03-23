package com.srecko.reddit.controller;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.SubredditService;
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

/**
 * The type Subreddit controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/subreddits")
public class SubredditController {

  private final SubredditService subredditService;

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
    return ResponseEntity.ok(subredditService.getAll());
  }

  /**
   * Gets subreddit.
   *
   * @param id the id
   * @return the subreddit
   */
  @GetMapping("/{subredditId}")
  public ResponseEntity<Subreddit> getSubreddit(@PathVariable("subredditId") Long id) {
    return ResponseEntity.ok(subredditService.getSubredditById(id));
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
    return ResponseEntity.created(uri).body(subredditService.save(subredditDto));
  }

  /**
   * Delete subreddit.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("/{subredditId}")
  public ResponseEntity<Subreddit> delete(@PathVariable("subredditId") Long id) {
    return ResponseEntity.ok(subredditService.delete(id));
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
    return ResponseEntity.ok(subredditService.update(subredditDto));
  }
}
