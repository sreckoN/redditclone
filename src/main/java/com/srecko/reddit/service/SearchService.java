package com.srecko.reddit.service;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
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
  Page<User> searchUsers(String query, Pageable pageable);

  /**
   * Search subreddits page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Subreddit> searchSubreddits(String query, Pageable pageable);

  /**
   * Search posts page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Post> searchPosts(String query, Pageable pageable);

  /**
   * Search comments page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<Comment> searchComments(String query, Pageable pageable);
}