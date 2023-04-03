package com.srecko.reddit.service;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
  Page<UserDto> searchUsers(String query, Pageable pageable);

  /**
   * Search subreddits page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<SubredditDto> searchSubreddits(String query, Pageable pageable);

  /**
   * Search posts page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<PostDto> searchPosts(String query, Pageable pageable);


  /**
   * Search posts in subreddit page.
   *
   * @param subredditId the subreddit id
   * @param query       the query
   * @param pageable    the pageable
   * @return the page
   */
  Page<PostDto> searchPostsInSubreddit(Long subredditId, String query, Pageable pageable);

  /**
   * Search comments page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<CommentDto> searchComments(String query, Pageable pageable);
}
