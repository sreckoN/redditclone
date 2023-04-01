package com.srecko.reddit.service;

import com.srecko.reddit.assembler.PageRequestAssembler;
import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.post.PostNotFoundException;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Post service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {UserNotFoundException.class, SubredditNotFoundException.class,
    PostNotFoundException.class})
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final SubredditRepository subredditRepository;

  private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);

  /**
   * Instantiates a new Post service.
   *
   * @param postRepository      the post repository
   * @param userRepository      the user repository
   * @param subredditRepository the subreddit repository
   */
  @Autowired
  public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
      SubredditRepository subredditRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.subredditRepository = subredditRepository;
  }

  @Override
  public Post save(CreatePostDto createPostDto) {
    UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    logger.info("Saving post to the database");
    Optional<User> user = userRepository.findUserByUsername(userMediator.getUsername());
    if (user.isPresent()) {
      Optional<Subreddit> subreddit = subredditRepository.findById(createPostDto.getSubredditId());
      if (subreddit.isPresent()) {
        Post post = new Post(user.get(), createPostDto.getTitle(), createPostDto.getText(),
            subreddit.get());
        user.get().getPosts().add(post);
        subreddit.get().getPosts().add(post);
        return post;
      } else {
        throw new SubredditNotFoundException(createPostDto.getSubredditId());
      }
    } else {
      throw new UserNotFoundException(userMediator.getUsername());
    }
  }

  @Override
  public Page<Post> getAllPosts(Pageable pageable) {
    logger.info("Getting all posts");
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
        Sort.by(Direction.ASC, "dateOfCreation"));
    return postRepository.findAll(pageRequest);
  }

  @Override
  public Post getPost(Long postId) {
    logger.info("Getting post: {}", postId);
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isPresent()) {
      return postOptional.get();
    } else {
      throw new PostNotFoundException(postId);
    }
  }

  @Override
  public Page<Post> getAllPostsForSubreddit(Long subredditId, Pageable pageable) {
    logger.info("Getting posts for subreddit: {}", subredditId);
    Optional<Subreddit> subredditOptional = subredditRepository.findById(subredditId);
    if (subredditOptional.isPresent()) {
      PageRequest pageRequest =
          PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
          Sort.by(Direction.ASC, "dateOfCreation"));
      return postRepository.findAllBySubreddit(subredditOptional.get(), pageRequest);
    } else {
      throw new SubredditNotFoundException(subredditId);
    }
  }

  @Override
  public Page<Post> getAllPostsForUser(String username, Pageable pageable) {
    logger.info("Getting posts for user: {}", username);
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    if (userOptional.isPresent()) {
      PageRequest pageRequest =
          PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
          Sort.by(Direction.ASC, "dateOfCreation"));
      return postRepository.findAllByUser(userOptional.get(), pageRequest);
    } else {
      throw new UserNotFoundException(username);
    }
  }

  @Override
  public Post delete(Long postId) {
    logger.info("Deleting post: {}", postId);
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isPresent()) {
      postRepository.deleteById(postId);
      return postOptional.get();
    } else {
      throw new PostNotFoundException(postId);
    }
  }

  @Override
  public Post update(UpdatePostDto postDto) {
    logger.info("Updating post: {}", postDto.getPostId());
    Optional<Post> postOptional = postRepository.findById(postDto.getPostId());
    if (postOptional.isPresent()) {
      Post post = postOptional.get();
      post.setTitle(postDto.getTitle());
      post.setText(postDto.getText());
      post.setId(postDto.getPostId());
      return postRepository.save(post);
    } else {
      throw new PostNotFoundException(postDto.getPostId());
    }
  }
}
