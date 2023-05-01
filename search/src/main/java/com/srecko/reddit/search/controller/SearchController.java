package com.srecko.reddit.search.controller;

import com.srecko.reddit.search.dto.CommentDto;
import com.srecko.reddit.search.dto.PostDto;
import com.srecko.reddit.search.dto.SubredditDto;
import com.srecko.reddit.search.dto.UserDto;
import com.srecko.reddit.search.service.SearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Search controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {

  private final SearchService searchService;

  private static final Logger logger = LogManager.getLogger(SearchController.class);

  /**
   * Instantiates a new Search controller.
   *
   * @param searchService the search service
   */
  @Autowired
  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  /**
   * Search users.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the response entity
   */
  @GetMapping("/users")
  public ResponseEntity<PagedModel<EntityModel<UserDto>>> searchUsers(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
    PagedModel<EntityModel<UserDto>> page = searchService.searchUsers(query, pageable);
    logger.info("Returning a page {}/{} of users",
        page.getMetadata().getNumber(), page.getMetadata().getTotalPages());
    return ResponseEntity.ok(page);
  }

  /**
   * Search subreddits.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the response entity
   */
  @GetMapping("/subreddits")
  public ResponseEntity<PagedModel<EntityModel<SubredditDto>>> searchSubreddits(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
    PagedModel<EntityModel<SubredditDto>> page = searchService.searchSubreddits(query, pageable);
    logger.info("Returning a page {}/{} of subreddits",
        page.getMetadata().getNumber(), page.getMetadata().getTotalPages());
    return ResponseEntity.ok(page);
  }

  /**
   * Search posts.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the response entity
   */
  @GetMapping("/posts")
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> searchPosts(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable) {
    PagedModel<EntityModel<PostDto>> page = searchService.searchPosts(query, pageable);
    logger.info("Returning a page {}/{} of posts",
        page.getMetadata().getNumber(), page.getMetadata().getTotalPages());
    return ResponseEntity.ok(page);
  }

  /**
   * Search posts in subreddit response entity.
   *
   * @param subredditId the subreddit id
   * @param query       the query
   * @param pageable    the pageable
   * @return the response entity
   */
  @GetMapping("/posts/subreddit/{subredditId}")
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> searchPostsInSubreddit(
      @PathVariable("subredditId") Long subredditId,
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable) {
    PagedModel<EntityModel<PostDto>> page = searchService
        .searchPostsInSubreddit(subredditId, query, pageable);
    logger.info("Returning a page {}/{} of posts for subreddit: {}",
        page.getMetadata().getNumber(), page.getMetadata().getTotalPages(), subredditId);
    return ResponseEntity.ok(page);
  }

  /**
   * Search comment.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the response entity
   */
  @GetMapping("/comments")
  public ResponseEntity<PagedModel<EntityModel<CommentDto>>> searchComment(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable) {
    PagedModel<EntityModel<CommentDto>> page = searchService.searchComments(query, pageable);
    logger.info("Returning a page {}/{} of comments",
        page.getMetadata().getNumber(), page.getMetadata().getTotalPages());
    return ResponseEntity.ok(page);
  }
}