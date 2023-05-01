package com.srecko.reddit.posts.service;

import com.srecko.reddit.posts.dto.CreatePostRequest;
import com.srecko.reddit.posts.dto.PostDto;
import com.srecko.reddit.posts.dto.UpdatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Post service.
 *
 * @author Srecko Nikolic
 */
public interface PostService {

  /**
   * Save post.
   *
   * @param postDto the post dto
   * @return the post
   */
  PostDto save(CreatePostRequest postDto);

  /**
   * Gets all posts.
   *
   * @param pageable the pageable
   * @return the all posts
   */
  Page<PostDto> getAllPosts(Pageable pageable);

  /**
   * Gets post.
   *
   * @param postId the post id
   * @return the post
   */
  PostDto getPost(Long postId);

  /**
   * Gets all posts for subreddit.
   *
   * @param subredditId the subreddit id
   * @param pageable    the pageable
   * @return the all posts for subreddit
   */
  Page<PostDto> getAllPostsForSubreddit(Long subredditId, Pageable pageable);

  /**
   * Gets all posts for user.
   *
   * @param username the username
   * @param pageable the pageable
   * @return the all posts for user
   */
  Page<PostDto> getAllPostsForUser(String username, Pageable pageable);

  /**
   * Delete post.
   *
   * @param postId the post id
   * @return the post
   */
  PostDto delete(Long postId);

  /**
   * Update post.
   *
   * @param postDto the post dto
   * @return the post
   */
  PostDto update(UpdatePostRequest postDto);

  /**
   * Check if exists.
   *
   * @param postId the post id
   */
  void checkIfExists(Long postId);

  /**
   * Search page.
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
}
