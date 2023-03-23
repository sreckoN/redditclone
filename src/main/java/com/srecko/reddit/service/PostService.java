package com.srecko.reddit.service;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.entity.Post;
import java.util.List;

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
   * @return the all posts
   */
  List<Post> getAllPosts();

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
   * @return the all posts for subreddit
   */
  List<Post> getAllPostsForSubreddit(Long subredditId);

  /**
   * Gets all posts for user.
   *
   * @param username the username
   * @return the all posts for user
   */
  List<Post> getAllPostsForUser(String username);

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
