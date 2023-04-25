package com.srecko.reddit.posts.service;

import com.srecko.reddit.posts.assembler.PageRequestAssembler;
import com.srecko.reddit.posts.dto.CreatePostRequest;
import com.srecko.reddit.posts.dto.ModelPageToDtoPageConverter;
import com.srecko.reddit.posts.dto.PostDto;
import com.srecko.reddit.posts.dto.UpdatePostRequest;
import com.srecko.reddit.posts.entity.Post;
import com.srecko.reddit.posts.exception.PostNotFoundException;
import com.srecko.reddit.posts.repository.PostRepository;
import com.srecko.reddit.posts.service.client.SubredditsFeignClient;
import com.srecko.reddit.posts.service.client.UsersFeignClient;
import java.util.List;
import java.util.Optional;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Post service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {PostNotFoundException.class})
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UsersFeignClient usersFeignClient;
  private final SubredditsFeignClient subredditsFeignClient;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);

  /**
   * Instantiates a new Post service.
   *
   * @param postRepository        the post repository
   * @param usersFeignClient      the users feign client
   * @param subredditsFeignClient the subreddits feign client
   * @param modelMapper           the model mapper
   */
  @Autowired
  public PostServiceImpl(PostRepository postRepository,
      UsersFeignClient usersFeignClient,
      SubredditsFeignClient subredditsFeignClient,
      ModelMapper modelMapper) {
    this.postRepository = postRepository;
    this.usersFeignClient = usersFeignClient;
    this.subredditsFeignClient = subredditsFeignClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public PostDto save(CreatePostRequest createPostRequest) {
    /*UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    logger.info("Saving post to the database");
    Long userId = usersFeignClient.getUserId(userMediator.getUsername());*/
    Long userId = usersFeignClient.getUserId("username");
    subredditsFeignClient.checkIfSubredditExists(createPostRequest.getSubredditId());
    Post post = new Post(userId, createPostRequest.getTitle(), createPostRequest.getText(),
        createPostRequest.getSubredditId());
    Post saved = postRepository.save(post);
    return modelMapper.map(saved, PostDto.class);
  }

  @Override
  public Page<PostDto> getAllPosts(Pageable pageable) {
    logger.info("Getting all posts");
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
            Sort.by(Direction.ASC, "dateOfCreation"));
    Page<Post> posts = postRepository.findAll(pageRequest);
    return ModelPageToDtoPageConverter.convertPosts(pageable, posts, modelMapper);
  }

  @Override
  public PostDto getPost(Long postId) {
    logger.info("Getting post: {}", postId);
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isPresent()) {
      return modelMapper.map(postOptional.get(), PostDto.class);
    } else {
      throw new PostNotFoundException(postId);
    }
  }

  @Override
  public Page<PostDto> getAllPostsForSubreddit(Long subredditId, Pageable pageable) {
    logger.info("Getting posts for subreddit: {}", subredditId);
    subredditsFeignClient.checkIfSubredditExists(subredditId);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
            Sort.by(Direction.ASC, "dateOfCreation"));
    Page<Post> posts = postRepository.findAllBySubredditId(subredditId,
        pageRequest);
    return ModelPageToDtoPageConverter.convertPosts(pageable, posts, modelMapper);
  }

  // todo: change to userId
  @Override
  public Page<PostDto> getAllPostsForUser(String username, Pageable pageable) {
    logger.info("Getting posts for user: {}", username);
    Long userId = usersFeignClient.getUserId(username);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("dateOfCreation", "title", "votes"),
            Sort.by(Direction.ASC, "dateOfCreation"));
    Page<Post> posts = postRepository.findAllByUserId(userId, pageRequest);
    return ModelPageToDtoPageConverter.convertPosts(pageable, posts, modelMapper);
  }

  @Override
  public PostDto delete(Long postId) {
    logger.info("Deleting post: {}", postId);
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isPresent()) {
      postRepository.deleteById(postId);
      return modelMapper.map(postOptional.get(), PostDto.class);
    } else {
      throw new PostNotFoundException(postId);
    }
  }

  @Override
  public PostDto update(UpdatePostRequest postDto) {
    logger.info("Updating post: {}", postDto.getPostId());
    Optional<Post> postOptional = postRepository.findById(postDto.getPostId());
    if (postOptional.isPresent()) {
      Post post = postOptional.get();
      post.setTitle(postDto.getTitle());
      post.setText(postDto.getText());
      post.setId(postDto.getPostId());
      post = postRepository.save(post);
      return modelMapper.map(post, PostDto.class);
    } else {
      throw new PostNotFoundException(postDto.getPostId());
    }
  }

  @Override
  public void checkIfExists(Long postId) {
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isEmpty()) {
      throw new PostNotFoundException(postId);
    }
  }
}
