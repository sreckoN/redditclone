package com.srecko.reddit.service;

import com.srecko.reddit.assembler.PageRequestAssembler;
import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserDto;
import com.srecko.reddit.dto.util.ModelPageToDtoPageConverter;
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
import org.modelmapper.ModelMapper;
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
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(SearchServiceImpl.class);

  /**
   * Instantiates a new Search service.
   *
   * @param postRepository      the post repository
   * @param commentRepository   the comment repository
   * @param userRepository      the user repository
   * @param subredditRepository the subreddit repository
   * @param modelMapper         the model mapper
   */
  @Autowired
  public SearchServiceImpl(PostRepository postRepository, CommentRepository commentRepository,
      UserRepository userRepository, SubredditRepository subredditRepository,
      ModelMapper modelMapper) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.subredditRepository = subredditRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public Page<UserDto> searchUsers(String query, Pageable pageable) {
    logger.info("Searching for usernames that match query: {}", query);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("username"),
        Sort.by(Direction.ASC, "username"));
    Page<User> users = userRepository.findByUsernameContainingIgnoreCase(
        query, pageRequest);
    return ModelPageToDtoPageConverter.convertUsers(pageable, users, modelMapper);
  }

  @Override
  public Page<SubredditDto> searchSubreddits(String query, Pageable pageable) {
    logger.info("Searching for subreddits that match query: {}", query);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("name"),
        Sort.by(Direction.ASC, "name"));
    Page<Subreddit> subreddits = subredditRepository.findByNameContainingIgnoreCase(
        query, pageRequest);
    return ModelPageToDtoPageConverter.convertSubreddits(pageable, subreddits, modelMapper);
  }

  @Override
  public Page<PostDto> searchPosts(String query, Pageable pageable) {
    logger.info("Searching for posts that match query: {}", query);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
        Sort.by(Direction.ASC, "dateOfCreation"));
    Page<Post> posts = postRepository.findByTitleContainingIgnoreCase(query,
        pageRequest);
    return ModelPageToDtoPageConverter.convertPosts(pageable, posts, modelMapper);
  }

  @Override
  public Page<PostDto> searchPostsInSubreddit(Long subredditId, String query, Pageable pageable) {
    logger.info("Searching for posts in subreddit with id {} that match query: {}",
        subredditId, query);
    if (!subredditRepository.existsById(subredditId)) {
      throw new SubredditNotFoundException(subredditId);
    }
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
        Sort.by(Direction.ASC, "dateOfCreation"));
    Page<Post> posts = postRepository
        .findBySubredditIdAndTitleContainingIgnoreCase(subredditId, query, pageRequest);
    return ModelPageToDtoPageConverter.convertPosts(pageable, posts, modelMapper);
  }

  @Override
  public Page<CommentDto> searchComments(String query, Pageable pageable) {
    logger.info("Searching for comments that match query: {}", query);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
        Sort.by(Direction.ASC, "text"));
    Page<Comment> comments = commentRepository.findByTextContainingIgnoreCase(
        query, pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, comments, modelMapper);
  }
}