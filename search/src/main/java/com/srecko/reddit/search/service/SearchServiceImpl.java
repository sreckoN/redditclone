package com.srecko.reddit.search.service;

import com.srecko.reddit.search.dto.CommentDto;
import com.srecko.reddit.search.dto.PostDto;
import com.srecko.reddit.search.dto.SubredditDto;
import com.srecko.reddit.search.dto.UserDto;
import com.srecko.reddit.search.service.client.CommentsFeignClient;
import com.srecko.reddit.search.service.client.PostsFeignClient;
import com.srecko.reddit.search.service.client.SubredditsFeignClient;
import com.srecko.reddit.search.service.client.UsersFeignClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

/**
 * The type Search service.
 *
 * @author Srecko Nikolic
 */
@Service
public class SearchServiceImpl implements SearchService {

  private final UsersFeignClient usersFeignClient;
  private final SubredditsFeignClient subredditsFeignClient;
  private final PostsFeignClient postsFeignClient;
  private final CommentsFeignClient commentsFeignClient;

  private static final Logger logger = LogManager.getLogger(SearchServiceImpl.class);

  /**
   * Instantiates a new Search service.
   *
   * @param usersFeignClient      the users feign client
   * @param subredditsFeignClient the subreddits feign client
   * @param postsFeignClient      the posts feign client
   * @param commentsFeignClient   the comments feign client
   */
  @Autowired
  public SearchServiceImpl(UsersFeignClient usersFeignClient,
      SubredditsFeignClient subredditsFeignClient,
      PostsFeignClient postsFeignClient,
      CommentsFeignClient commentsFeignClient) {
    this.usersFeignClient = usersFeignClient;
    this.subredditsFeignClient = subredditsFeignClient;
    this.postsFeignClient = postsFeignClient;
    this.commentsFeignClient = commentsFeignClient;
  }

  @Override
  public PagedModel<EntityModel<UserDto>> searchUsers(String query, Pageable pageable) {
    logger.info("Searching for usernames that match query: {}", query);
    return usersFeignClient.searchUsers(query, pageable);
  }

  @Override
  public PagedModel<EntityModel<SubredditDto>> searchSubreddits(String query, Pageable pageable) {
    logger.info("Searching for subreddits that match query: {}", query);
    return subredditsFeignClient.searchSubreddits(query, pageable);
  }

  @Override
  public PagedModel<EntityModel<PostDto>> searchPosts(String query, Pageable pageable) {
    logger.info("Searching for posts that match query: {}", query);
    return postsFeignClient.searchPosts(query, pageable);
  }

  @Override
  public PagedModel<EntityModel<PostDto>> searchPostsInSubreddit(Long subredditId,
      String query, Pageable pageable) {
    logger.info("Searching for posts in subreddit with id {} that match query: {}",
        subredditId, query);
    return postsFeignClient.searchPostsInSubreddit(subredditId, query, pageable);
  }

  @Override
  public PagedModel<EntityModel<CommentDto>> searchComments(String query, Pageable pageable) {
    logger.info("Searching for comments that match query: {}", query);
    return commentsFeignClient.searchComments(query, pageable);
  }
}