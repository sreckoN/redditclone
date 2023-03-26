package com.srecko.reddit.service;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The type Search service.
 *
 * @author Srecko Nikolic
 */
@Service
public class SearchServiceImpl implements SearchService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final SubredditRepository subredditRepository;

  /**
   * Instantiates a new Search service.
   *
   * @param postRepository      the post repository
   * @param commentRepository   the comment repository
   * @param userRepository      the user repository
   * @param subredditRepository the subreddit repository
   */
  @Autowired
  public SearchServiceImpl(PostRepository postRepository, CommentRepository commentRepository,
      UserRepository userRepository, SubredditRepository subredditRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.subredditRepository = subredditRepository;
  }

  @Override
  public Page<User> searchUsers(String query, Pageable pageable) {
    PageRequest pageRequest;
    if (pageable.getSort().get().anyMatch(order -> order.getProperty().equals("username"))) {
      pageRequest =
          PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    } else {
      pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
          Sort.by(Sort.Direction.ASC, "username"));
    }
    return userRepository.findByUsernameContainingIgnoreCase(query, pageRequest);
  }

  @Override
  public Page<Subreddit> searchSubreddits(String query, Pageable pageable) {
    PageRequest pageRequest;
    if (pageable.getSort().get().anyMatch(order -> order.getProperty().equals("name"))) {
      pageRequest =
          PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    } else {
      pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
          Sort.by(Sort.Direction.ASC, "name"));
    }
    return subredditRepository.findByNameContainingIgnoreCase(query, pageRequest);
  }

  @Override
  public Page<Post> searchPosts(String query, Pageable pageable) {
    PageRequest pageRequest;
    if (pageable.getSort().get().anyMatch(
        order -> order.getProperty().equals("dateOfCreation") || order.getProperty().equals("title")
            || order.getProperty().equals("votes"))) {
      pageRequest =
          PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    } else {
      pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
          Sort.by(Sort.Direction.ASC, "dateOfCreation"));
    }
    return postRepository.findByTitleContainingIgnoreCase(query, pageRequest);
  }

  @Override
  public Page<Comment> searchComments(String query, Pageable pageable) {
    PageRequest pageRequest;
    if (pageable.getSort().get().anyMatch(
        order -> order.getProperty().equals("text") || order.getProperty().equals("created"))) {
      pageRequest = PageRequest
          .of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    } else {
      pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
          Sort.by(Sort.Direction.ASC, "text"));
    }
    return commentRepository.findByTextContainingIgnoreCase(query, pageRequest);
  }
}