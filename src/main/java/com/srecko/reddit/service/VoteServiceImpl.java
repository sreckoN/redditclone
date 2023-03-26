package com.srecko.reddit.service;

import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.entity.Vote;
import com.srecko.reddit.entity.VoteComment;
import com.srecko.reddit.entity.VotePost;
import com.srecko.reddit.entity.VoteType;
import com.srecko.reddit.exception.comment.CommentNotFoundException;
import com.srecko.reddit.exception.post.PostNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.exception.vote.VoteNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.UserRepository;
import com.srecko.reddit.repository.VoteRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Vote service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {PostNotFoundException.class, UserNotFoundException.class,
    VoteNotFoundException.class})
public class VoteServiceImpl implements VoteService {

  private final VoteRepository voteRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  /**
   * Instantiates a new Vote service.
   *
   * @param voteRepository    the vote repository
   * @param userRepository    the user repository
   * @param postRepository    the post repository
   * @param commentRepository the comment repository
   */
  @Autowired
  public VoteServiceImpl(VoteRepository voteRepository, UserRepository userRepository,
      PostRepository postRepository, CommentRepository commentRepository) {
    this.voteRepository = voteRepository;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
  }

  @Override
  public Vote savePostVote(VotePostDto voteDto) {
    UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Optional<User> userOptional = userRepository.findUserByUsername(userMediator.getUsername());
    if (userOptional.isPresent()) {
      Optional<Post> postOptional = postRepository.findById(voteDto.getPostId());
      if (postOptional.isPresent()) {
        Post post = postOptional.get();
        post.setVotes(post.getVotes() + voteDto.getType().getVal());
        Vote vote = new VotePost(userOptional.get(), voteDto.getType(), postOptional.get());
        voteRepository.save(vote);
        return vote;
      } else {
        throw new PostNotFoundException(voteDto.getPostId());
      }
    } else {
      throw new UserNotFoundException(userMediator.getUsername());
    }
  }

  @Override
  public Vote saveCommentVote(VoteCommentDto voteDto) {
    UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Optional<User> userOptional = userRepository.findUserByUsername(userMediator.getUsername());
    if (userOptional.isPresent()) {
      Optional<Comment> commentOptional = commentRepository.findById(voteDto.getCommentId());
      if (commentOptional.isPresent()) {
        Comment comment = commentOptional.get();
        comment.setVotes(comment.getVotes() + voteDto.getType().getVal());
        Vote vote = new VoteComment(userOptional.get(), voteDto.getType(), commentOptional.get());
        voteRepository.save(vote);
        return vote;
      } else {
        throw new CommentNotFoundException(voteDto.getCommentId());
      }
    } else {
      throw new UserNotFoundException(userMediator.getUsername());
    }
  }

  @Override
  public Vote deletePostVote(Long id) {
    Optional<Vote> voteOptional = voteRepository.findById(id);
    if (voteOptional.isPresent()) {
      VotePost vote = (VotePost) voteOptional.get();
      Optional<Post> postOptional = postRepository.findById(vote.getPost().getId());
      if (postOptional.isPresent()) {
        Post post = postOptional.get();
        int change = (vote.getType().equals(VoteType.UPVOTE)) ? -1 : 1;
        post.setVotes(post.getVotes() + change);
        voteRepository.delete(vote);
        return vote;
      } else {
        throw new PostNotFoundException(vote.getPost().getId());
      }
    } else {
      throw new VoteNotFoundException(id);
    }
  }

  @Override
  public Vote deleteCommentVote(Long id) {
    Optional<Vote> voteOptional = voteRepository.findById(id);
    if (voteOptional.isPresent()) {
      VoteComment vote = (VoteComment) voteOptional.get();
      Optional<Comment> commentOptional = commentRepository.findById(vote.getComment().getId());
      if (commentOptional.isPresent()) {
        Comment comment = commentOptional.get();
        int change = (vote.getType().equals(VoteType.UPVOTE)) ? -1 : 1;
        comment.setVotes(comment.getVotes() + change);
        voteRepository.delete(vote);
        return vote;
      } else {
        throw new PostNotFoundException(vote.getComment().getId());
      }
    } else {
      throw new VoteNotFoundException(id);
    }
  }
}
