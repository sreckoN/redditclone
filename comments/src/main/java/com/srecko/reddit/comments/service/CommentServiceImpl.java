package com.srecko.reddit.comments.service;

import com.srecko.reddit.comments.assembler.PageRequestAssembler;
import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.dto.ModelPageToDtoPageConverter;
import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.exception.CommentNotFoundException;
import com.srecko.reddit.comments.repository.CommentRepository;
import com.srecko.reddit.comments.service.client.PostFeignClient;
import com.srecko.reddit.comments.service.client.UsersFeignClient;
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
 * The type Comment service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {CommentNotFoundException.class})
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final UsersFeignClient usersFeignClient;
  private final PostFeignClient postFeignClient;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);

  /**
   * Instantiates a new Comment service.
   *
   * @param commentRepository the comment repository
   * @param usersFeignClient  the users feign client
   * @param postFeignClient   the post feign client
   * @param modelMapper       the model mapper
   */
  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      UsersFeignClient usersFeignClient,
      PostFeignClient postFeignClient,
      ModelMapper modelMapper) {
    this.commentRepository = commentRepository;
    this.usersFeignClient = usersFeignClient;
    this.postFeignClient = postFeignClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public Page<CommentDto> getAllCommentsForPost(Long postId, Pageable pageable) {
    logger.info("Getting all comments for post: {}", postId);
    postFeignClient.checkIfPostExists(postId);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
            Sort.by(Direction.ASC, "text"));
    Page<Comment> comments = commentRepository.findAllByPostId(postId, pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, comments, modelMapper);
  }

  @Override
  public Page<CommentDto> getAllCommentsForUsername(String username, Pageable pageable) {
    logger.info("Getting all comments for user: {}", username);
    Long userId = usersFeignClient.getUserId(username);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
            Sort.by(Direction.ASC, "text"));
    Page<Comment> comments = commentRepository.findAllByUserId(userId, pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, comments, modelMapper);
  }

  @Override
  public CommentDto save(CommentRequest commentRequest) {
    /*UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Long userId = usersFeignClient.getUserId(userMediator.getUsername());*/
    Long userId = usersFeignClient.getUserId("username");
    postFeignClient.checkIfPostExists(commentRequest.getPostId());
    Comment comment = new Comment(userId, commentRequest.getText(), commentRequest.getPostId());
    // todo: increase comment counter for a post
    Comment saved = commentRepository.save(comment);
    logger.info("Saved new comment to database: {}", comment.getId());
    return modelMapper.map(saved, CommentDto.class);
  }

  @Override
  public CommentDto delete(Long commentId) {
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isPresent()) {
      Comment comment = commentOptional.get();
      logger.info("Deleting comment: {}", comment.getId());
      commentRepository.delete(comment);
      return modelMapper.map(comment, CommentDto.class);
    } else {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public CommentDto getComment(Long commentId) {
    logger.info("Getting comment: {}", commentId);
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isPresent()) {
      return modelMapper.map(commentOptional.get(), CommentDto.class);
    } else {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public Page<CommentDto> getAllComments(Pageable pageable) {
    logger.info("Getting all comments");
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
        Sort.by(Direction.ASC, "text"));
    Page<Comment> comments = commentRepository.findAll(pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, comments, modelMapper);
  }

  @Override
  public void checkIfExists(Long commentId) {
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isEmpty()) {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public Page<CommentDto> search(String query, Pageable pageable) {
    logger.info("Searching for comments that match query: {}", query);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable,
        List.of("text", "created"),
        Sort.by(Direction.ASC, "text"));
    Page<Comment> users = commentRepository.findByTextContainingIgnoreCase(query,
        pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, users, modelMapper);
  }
}