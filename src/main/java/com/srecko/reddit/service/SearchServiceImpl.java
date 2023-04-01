package com.srecko.reddit.service;

import com.srecko.reddit.assembler.PageRequestAssembler;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

  private static final Logger logger = LogManager.getLogger(SearchServiceImpl.class);

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
    logger.info("Searching for usernames that match query: {}", query);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("username"),
        Sort.by(Direction.ASC, "username"));
    return userRepository.findByUsernameContainingIgnoreCase(query, pageRequest);
  }

  @Override
  public Page<Subreddit> searchSubreddits(String query, Pageable pageable) {
    logger.info("Searching for subreddits that match query: {}", query);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("name"),
        Sort.by(Direction.ASC, "name"));
    return subredditRepository.findByNameContainingIgnoreCase(query, pageRequest);
  }

  @Override
  public Page<Post> searchPosts(String query, Pageable pageable) {
    logger.info("Searching for posts that match query: {}", query);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
        Sort.by(Direction.ASC, "dateOfCreation"));
    return postRepository.findByTitleContainingIgnoreCase(query, pageRequest);
  }

  @Override
  public Page<Post> searchPostsInSubreddit(Long subredditId, String query, Pageable pageable) {
    logger.info("Searching for posts in subreddit with id {} that match query: {}",
        subredditId, query);
    if (!subredditRepository.existsById(subredditId)) {
      throw new SubredditNotFoundException(subredditId);
    }
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
        Sort.by(Direction.ASC, "dateOfCreation"));
    return postRepository
        .findBySubredditIdAndTitleContainingIgnoreCase(subredditId, query, pageRequest);
  }

  @Override
  public Page<Comment> searchComments(String query, Pageable pageable) {
    logger.info("Searching for comments that match query: {}", query);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
        Sort.by(Direction.ASC, "text"));
    return commentRepository.findByTextContainingIgnoreCase(query, pageRequest);
  }
}