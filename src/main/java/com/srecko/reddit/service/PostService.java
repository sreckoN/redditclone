package com.srecko.reddit.service;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.entity.Post;
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
  Post save(CreatePostDto postDto);

  /**
   * Gets all posts.
   *
   * @param pageable the pageable
   * @return the all posts
   */
  Page<Post> getAllPosts(Pageable pageable);

  /**
   * Gets post.
   *
   * @param postId the post id
   * @return the post
   */
  Post getPost(Long postId);

  /**
   * Gets all posts for subreddit.
   *
   * @param subredditId the subreddit id
   * @param pageable    the pageable
   * @return the all posts for subreddit
   */
  Page<Post> getAllPostsForSubreddit(Long subredditId, Pageable pageable);

  /**
   * Gets all posts for user.
   *
   * @param username the username
   * @param pageable the pageable
   * @return the all posts for user
   */
  Page<Post> getAllPostsForUser(String username, Pageable pageable);

  /**
   * Delete post.
   *
   * @param postId the post id
   * @return the post
   */
  Post delete(Long postId);

  /**
   * Update post.
   *
   * @param postDto the post dto
   * @return the post
   */
  Post update(UpdatePostDto postDto);
}
