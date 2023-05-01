package com.srecko.reddit.search.service;

import com.srecko.reddit.search.dto.CommentDto;
import com.srecko.reddit.search.dto.PostDto;
import com.srecko.reddit.search.dto.SubredditDto;
import com.srecko.reddit.search.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * The interface Search service.
 *
 * @author Srecko Nikolic
 */
public interface SearchService {

  /**
   * Search users page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  PagedModel<EntityModel<UserDto>> searchUsers(String query, Pageable pageable);

  /**
   * Search subreddits page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  PagedModel<EntityModel<SubredditDto>> searchSubreddits(String query, Pageable pageable);

  /**
   * Search posts page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  PagedModel<EntityModel<PostDto>> searchPosts(String query, Pageable pageable);


  /**
   * Search posts in subreddit page.
   *
   * @param subredditId the subreddit id
   * @param query       the query
   * @param pageable    the pageable
   * @return the page
   */
  PagedModel<EntityModel<PostDto>> searchPostsInSubreddit(Long subredditId,
      String query, Pageable pageable);

  /**
   * Search comments page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  PagedModel<EntityModel<CommentDto>> searchComments(String query, Pageable pageable);
}
