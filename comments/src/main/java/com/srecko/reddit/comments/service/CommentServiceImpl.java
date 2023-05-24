package com.srecko.reddit.comments.service;

import com.srecko.reddit.comments.assembler.PageRequestAssembler;
import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.dto.ModelPageToDtoPageConverter;
import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.entity.CommentParentType;
import com.srecko.reddit.comments.exception.CommentNotFoundException;
import com.srecko.reddit.comments.repository.CommentRepository;
import com.srecko.reddit.comments.service.client.PostsFeignClient;
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
  private final PostsFeignClient postsFeignClient;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);

  /**
   * Instantiates a new Comment service.
   *
   * @param commentRepository the comment repository
   * @param usersFeignClient  the users feign client
   * @param postsFeignClient   the post feign client
   * @param modelMapper       the model mapper
   */
  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      UsersFeignClient usersFeignClient,
      PostsFeignClient postsFeignClient,
      ModelMapper modelMapper) {
    this.commentRepository = commentRepository;
    this.usersFeignClient = usersFeignClient;
    this.postsFeignClient = postsFeignClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public Page<CommentDto> getAllCommentsForPost(Long postId, Pageable pageable) {
    logger.info("Getting all comments for post: {}", postId);
    postsFeignClient.checkIfPostExists(postId);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
            Sort.by(Direction.ASC, "text"));
    Page<Comment> comments = commentRepository.findAllByParentTypeAndParentId(
        CommentParentType.POST, postId, pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, comments, modelMapper);
  }

  @Override
  public Page<CommentDto> getAllCommentsForComment(Long commentId, Pageable pageable) {
    logger.info("Getting all comments for comment: {}", commentId);
    checkIfExists(commentId);
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
            Sort.by(Direction.ASC, "text"));
    Page<Comment> comments = commentRepository.findAllByParentTypeAndParentId(
        CommentParentType.COMMENT, commentId, pageRequest);
    return ModelPageToDtoPageConverter.convertComments(pageable, comments, modelMapper);
  }

  @Override
  public Page<CommentDto> getAllCommentsForUser(Long userId, Pageable pageable) {
    logger.info("Getting all comments for user: {}", userId);
    usersFeignClient.checkIfExists(userId);
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
    CommentParentType parentType = commentRequest.getParentType();
    Long parentId = commentRequest.getParentId();
    logger.info("Creating a new comment for {} with id {}", parentType, parentId);
    boolean isPost = parentType.equals(CommentParentType.POST);
    if (isPost) {
      postsFeignClient.checkIfPostExists(parentId);
    } else {
      checkIfExists(parentId);
    }
    Comment comment = new Comment(userId, commentRequest.getText(), parentType, parentId);
    Comment saved = commentRepository.save(comment);
    if (isPost) {
      postsFeignClient.increaseCommentCounter(parentId);
    } else {
      increaseCommentCounter(parentId);
    }
    logger.info("Saved new comment to database: {}", saved.getId());
    return modelMapper.map(saved, CommentDto.class);
  }

  @Override
  public CommentDto delete(Long commentId) {
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isEmpty()) {
      throw new CommentNotFoundException(commentId);
    }
    Comment comment = commentOptional.get();
    logger.info("Deleting comment: {}", comment.getId());
    commentRepository.delete(comment);
    if (comment.getParentType().equals(CommentParentType.POST)) {
      postsFeignClient.decreaseCommentCounter(comment.getParentId());
    } else {
      decreaseCommentCounter(comment.getParentId());
    }
    return modelMapper.map(comment, CommentDto.class);
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

  @Override
  public void increaseCommentCounter(Long parentId) {
    updateCommentCounter(parentId, 1);
  }

  @Override
  public void decreaseCommentCounter(Long parentId) {
    updateCommentCounter(parentId, -1);
  }

  private void updateCommentCounter(Long parentId, int value) {
    Optional<Comment> commentOptional = commentRepository.findById(parentId);
    if (commentOptional.isEmpty()) {
      throw new CommentNotFoundException(parentId);
    }
    Comment comment = commentOptional.get();
    comment.setCommentsCounter(comment.getCommentsCounter() + value);
    commentRepository.save(comment);
  }
}