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

@Service
public class SearchServiceImpl implements SearchService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final SubredditRepository subredditRepository;

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
    if (pageable.getSort().get().anyMatch(order -> order.getProperty().equals("username"))) {
      return userRepository.findByUsernameContaining(query, pageable);
    }
    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by(Sort.Direction.ASC, "username"));
    return userRepository.findByUsernameContaining(query, pageRequest);
  }

  @Override
  public Page<Subreddit> searchSubreddits(String query, Pageable pageable) {
    if (pageable.getSort().get().anyMatch(order -> order.getProperty().equals("name"))) {
      return subredditRepository.findByNameContaining(query, pageable);
    }
    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by(Sort.Direction.ASC, "name"));
    return subredditRepository.findByNameContaining(query, pageRequest);
  }

  @Override
  public Page<Post> searchPosts(String query, Pageable pageable) {
    Sort sort = pageable.getSort();
    if (sort.get().anyMatch(
        order -> order.getProperty().equals("dateOfCreation") || order.getProperty().equals("title")
            || order.getProperty().equals("votes"))) {
      return postRepository.findByTitleContaining(query, pageable);
    }
    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by(Sort.Direction.ASC, "dateOfCreation"));
    return postRepository.findByTitleContaining(query, pageRequest);
  }

  @Override
  public Page<Comment> searchComments(String query, Pageable pageable) {
    if (pageable.getSort().get().anyMatch(
        order -> order.getProperty().equals("text") || order.getProperty().equals("created"))) {
      return commentRepository.findByTextContaining(query, pageable);
    }
    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by(Sort.Direction.ASC, "text"));
    return commentRepository.findByTextContaining(query, pageRequest);
  }
}