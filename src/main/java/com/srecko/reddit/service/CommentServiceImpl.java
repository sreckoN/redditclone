package com.srecko.reddit.service;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.CommentNotFoundException;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {UserNotFoundException.class, PostNotFoundException.class,
    CommentNotFoundException.class})
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
      UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<Comment> getAllCommentsForPost(Long postId) {
    Optional<Post> postOptional = postRepository.findById(postId);
    if (postOptional.isPresent()) {
      return commentRepository.findAllByPost(postOptional.get());
    } else {
      throw new PostNotFoundException("Post with id " + postId + " is not found.");
    }
  }

  @Override
  public List<Comment> getAllCommentsForUsername(String username) {
    Optional<User> optionalUser = userRepository.findUserByUsername(username);
    if (optionalUser.isPresent()) {
      return commentRepository.findAllByUser(optionalUser.get());
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
      commentRepository.delete(commentOptional.get());
      return commentOptional.get();
    } else {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public Comment getComment(Long commentId) {
    Optional<Comment> commentOptional = commentRepository.findById(commentId);
    if (commentOptional.isPresent()) {
      return commentOptional.get();
    } else {
      throw new CommentNotFoundException(commentId);
    }
  }

  @Override
  public List<Comment> getAllComments() {
    return commentRepository.findAll();
  }
}