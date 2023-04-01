package com.srecko.reddit.service;

import com.srecko.reddit.assembler.PageRequestAssembler;
import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.comment.CommentNotFoundException;
import com.srecko.reddit.exception.post.PostNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
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
 * The type Comment service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {UserNotFoundException.class, PostNotFoundException.class,
    CommentNotFoundException.class})
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);

  /**
   * Instantiates a new Comment service.
   *
   * @param commentRepository the comment repository
   * @param postRepository    the post repository
   * @param userRepository    the user repository
   */
  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
      UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Page<Comment> getAllCommentsForPost(Long postId, Pageable pageable) {
    logger.info("Getting all comments for post: {}", postId);
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isPresent()) {
      PageRequest pageRequest =
          PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
          Sort.by(Direction.ASC, "text"));
      return commentRepository.findAllByPost(postOptional.get(), pageRequest);
    } else {
      throw new PostNotFoundException("Post with id " + postId + " is not found.");
    }
  }

  @Override
  public Page<Comment> getAllCommentsForUsername(String username, Pageable pageable) {
    logger.info("Getting all comments for user: {}", username);
    Optional<User> optionalUser = userRepository.findUserByUsername(username);
    if (optionalUser.isPresent()) {
      PageRequest pageRequest =
          PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
          Sort.by(Direction.ASC, "text"));
      return commentRepository.findAllByUser(optionalUser.get(), pageRequest);
    } else {
      throw new UserNotFoundException(username);
    }
  }

  @Override
  public Comment save(CommentDto commentDto) {
    UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Optional<User> userOptional = userRepository.findUserByUsername(userMediator.getUsername());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Optional<Post> postOptional = postRepository.findById(commentDto.getPostId());
      if (postOptional.isPresent()) {
        Post post = postOptional.get();
        Comment comment = new Comment(user, commentDto.getText(), post);
        user.getComments().add(comment);
        post.getComments().add(comment);
        post.setCommentsCounter(post.getCommentsCounter());
        logger.info("Saved new comment to database: {}", comment.getId());
        return comment;
      } else {
        throw new PostNotFoundException(commentDto.getPostId());
      }
    } else {
      throw new UserNotFoundException(userMediator.getUsername());
    }
  }

  @Override
  public Comment delete(Long commentId) {
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isPresent()) {
      Comment comment = commentOptional.get();
      logger.info("Deleting comment: {}", comment.getId());
      commentRepository.delete(comment);
      return comment;
    } else {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public Comment getComment(Long commentId) {
    logger.info("Getting comment: {}", commentId);
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isPresent()) {
      return commentOptional.get();
    } else {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public Page<Comment> getAllComments(Pageable pageable) {
    logger.info("Getting all comments");
    PageRequest pageRequest =
        PageRequestAssembler.getPageRequest(pageable, List.of("text", "created"),
        Sort.by(Direction.ASC, "text"));
    return commentRepository.findAll(pageRequest);
  }
}