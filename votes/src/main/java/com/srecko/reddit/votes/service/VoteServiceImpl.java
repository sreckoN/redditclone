package com.srecko.reddit.votes.service;

import com.srecko.reddit.votes.dto.VoteCommentDto;
import com.srecko.reddit.votes.dto.VoteCommentRequest;
import com.srecko.reddit.votes.dto.VotePostDto;
import com.srecko.reddit.votes.dto.VotePostRequest;
import com.srecko.reddit.votes.entity.Vote;
import com.srecko.reddit.votes.entity.VoteComment;
import com.srecko.reddit.votes.entity.VotePost;
import com.srecko.reddit.votes.exception.VoteNotFoundException;
import com.srecko.reddit.votes.repository.VoteRepository;
import com.srecko.reddit.votes.service.client.CommentsFeignClient;
import com.srecko.reddit.votes.service.client.PostsFeignClient;
import com.srecko.reddit.votes.service.client.UsersFeignClient;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Vote service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {VoteNotFoundException.class})
public class VoteServiceImpl implements VoteService {

  private final VoteRepository voteRepository;
  private final UsersFeignClient usersFeignClient;
  private final PostsFeignClient postsFeignClient;
  private final CommentsFeignClient commentsFeignClient;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(VoteServiceImpl.class);

  /**
   * Instantiates a new Vote service.
   *
   * @param voteRepository      the vote repository
   * @param usersFeignClient    the users feign client
   * @param postsFeignClient    the posts feign client
   * @param commentsFeignClient the comments feign client
   * @param modelMapper         the model mapper
   */
  @Autowired
  public VoteServiceImpl(VoteRepository voteRepository,
      UsersFeignClient usersFeignClient,
      PostsFeignClient postsFeignClient,
      CommentsFeignClient commentsFeignClient,
      ModelMapper modelMapper) {
    this.voteRepository = voteRepository;
    this.usersFeignClient = usersFeignClient;
    this.postsFeignClient = postsFeignClient;
    this.commentsFeignClient = commentsFeignClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public VotePostDto savePostVote(VotePostRequest voteDto) {
    logger.info("Saving vote for post: {}", voteDto.getPostId());
    /*UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Long userId = usersFeignClient.getUserId(userMediator.getUsername());*/
    Long userId = usersFeignClient.getUserId("username");
    postsFeignClient.checkIfPostExists(voteDto.getPostId());
    Vote vote = new VotePost(userId, voteDto.getType(), voteDto.getPostId());
    voteRepository.save(vote);
    return modelMapper.map(vote, VotePostDto.class);
  }

  @Override
  public VoteCommentDto saveCommentVote(VoteCommentRequest voteDto) {
    logger.info("Saving vote for comment: {}", voteDto.getCommentId());
    /*UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Long userId = usersFeignClient.getUserId(userMediator.getUsername());*/
    Long userId = usersFeignClient.getUserId("username");
    commentsFeignClient.checkIfCommentExists(voteDto.getCommentId());
    Vote vote = new VoteComment(userId, voteDto.getType(), voteDto.getCommentId());
    voteRepository.save(vote);
    return modelMapper.map(vote, VoteCommentDto.class);
  }

  @Override
  public VotePostDto deletePostVote(Long id) {
    logger.info("Deleting vote for post: {}", id);
    Optional<Vote> voteOptional = voteRepository.findById(id);
    if (voteOptional.isPresent()) {
      VotePost vote = (VotePost) voteOptional.get();
      postsFeignClient.checkIfPostExists(vote.getPostId());
      voteRepository.delete(vote);
      return modelMapper.map(vote, VotePostDto.class);
    } else {
      throw new VoteNotFoundException(id);
    }
  }

  @Override
  public VoteCommentDto deleteCommentVote(Long voteId) {
    logger.info("Deleting vote for comment: {}", voteId);
    Optional<Vote> voteOptional = voteRepository.findById(voteId);
    if (voteOptional.isPresent()) {
      VoteComment vote = (VoteComment) voteOptional.get();
      commentsFeignClient.checkIfCommentExists(vote.getCommentId());
      voteRepository.delete(vote);
      return modelMapper.map(vote, VoteCommentDto.class);
    } else {
      throw new VoteNotFoundException(voteId);
    }
  }
}
