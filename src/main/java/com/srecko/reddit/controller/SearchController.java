package com.srecko.reddit.controller;

import com.srecko.reddit.assembler.CommentModelAssembler;
import com.srecko.reddit.assembler.PostModelAssembler;
import com.srecko.reddit.assembler.SubredditModelAssembler;
import com.srecko.reddit.assembler.UserModelAssembler;
import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserDto;
import com.srecko.reddit.service.SearchService;
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
  private final PostModelAssembler postModelAssembler;
  private final CommentModelAssembler commentModelAssembler;
  private final UserModelAssembler userModelAssembler;
  private final SubredditModelAssembler subredditModelAssembler;

  private static final Logger logger = LogManager.getLogger(SearchController.class);

  /**
   * Instantiates a new Search controller.
   *
   * @param searchService           the search service
   * @param postModelAssembler      the post model assembler
   * @param commentModelAssembler   the comment model assembler
   * @param userModelAssembler      the user model assembler
   * @param subredditModelAssembler the subreddit model assembler
   */
  @Autowired
  public SearchController(SearchService searchService, PostModelAssembler postModelAssembler,
      CommentModelAssembler commentModelAssembler, UserModelAssembler userModelAssembler,
      SubredditModelAssembler subredditModelAssembler) {
    this.searchService = searchService;
    this.postModelAssembler = postModelAssembler;
    this.commentModelAssembler = commentModelAssembler;
    this.userModelAssembler = userModelAssembler;
    this.subredditModelAssembler = subredditModelAssembler;
  }

  /**
   * Search users.
   *
   * @param query     the query
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the response entity
   */
  @GetMapping("/users")
  public ResponseEntity<PagedModel<EntityModel<UserDto>>> searchUsers(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<UserDto> assembler) {
    Page<UserDto> page = searchService.searchUsers(query, pageable);
    PagedModel<EntityModel<UserDto>> pagedModel = assembler.toModel(page, userModelAssembler);
    logger.info("Returning a page {} of users", page.getNumber());
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Search subreddits.
   *
   * @param query     the query
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the response entity
   */
  @GetMapping("/subreddits")
  public ResponseEntity<PagedModel<EntityModel<SubredditDto>>> searchSubreddits(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<SubredditDto> assembler) {
    Page<SubredditDto> page = searchService.searchSubreddits(query, pageable);
    PagedModel<EntityModel<SubredditDto>> pagedModel = assembler.toModel(page,
        subredditModelAssembler);
    logger.info("Returning a page {} of subreddits", page.getNumber());
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Search posts.
   *
   * @param query     the query
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the response entity
   */
  @GetMapping("/posts")
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> searchPosts(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<PostDto> assembler) {
    Page<PostDto> page = searchService.searchPosts(query, pageable);
    PagedModel<EntityModel<PostDto>> pagedModel = assembler.toModel(page, postModelAssembler);
    logger.info("Returning a page {} of posts", page.getNumber());
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Search posts in subreddit response entity.
   *
   * @param subredditId the subreddit id
   * @param query       the query
   * @param pageable    the pageable
   * @param assembler   the assembler
   * @return the response entity
   */
  @GetMapping("/posts/{subredditId}")
  public ResponseEntity<PagedModel<EntityModel<PostDto>>> searchPostsInSubreddit(
      @PathVariable("subredditId") Long subredditId,
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<PostDto> assembler) {
    Page<PostDto> page = searchService.searchPostsInSubreddit(subredditId, query, pageable);
    PagedModel<EntityModel<PostDto>> pagedModel = assembler.toModel(page, postModelAssembler);
    logger.info("Returning a page {} of posts for subreddit: {}", page.getNumber(), subredditId);
    return ResponseEntity.ok(pagedModel);
  }

  /**
   * Search comment.
   *
   * @param query     the query
   * @param pageable  the pageable
   * @param assembler the assembler
   * @return the response entity
   */
  @GetMapping("/comments")
  public ResponseEntity<PagedModel<EntityModel<CommentDto>>> searchComment(
      @RequestParam(name = "q") String query,
      @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
      PagedResourcesAssembler<CommentDto> assembler) {
    Page<CommentDto> page = searchService.searchComments(query, pageable);
    PagedModel<EntityModel<CommentDto>> pagedModel = assembler.toModel(page, commentModelAssembler);
    logger.info("Returning a page {} of comments", page.getNumber());
    return ResponseEntity.ok(pagedModel);
  }
}