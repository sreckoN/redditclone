package com.srecko.reddit.votes.service;

import com.srecko.reddit.votes.dto.VoteCommentDto;
import com.srecko.reddit.votes.dto.VoteCommentRequest;
import com.srecko.reddit.votes.dto.VotePostDto;
import com.srecko.reddit.votes.dto.VotePostRequest;

/**
 * The interface Vote service.
 *
 * @author Srecko Nikolic
 */
public interface VoteService {

  /**
   * Save post vote.
   *
   * @param voteDto the vote dto
   * @return the vote
   */
  VotePostDto savePostVote(VotePostRequest voteDto);

  /**
   * Delete post vote.
   *
   * @param id the id
   * @return the vote
   */
  VotePostDto deletePostVote(Long id);

  /**
   * Save comment vote.
   *
   * @param voteDto the vote dto
   * @return the vote
   */
  VoteCommentDto saveCommentVote(VoteCommentRequest voteDto);

  /**
   * Delete comment vote.
   *
   * @param id the id
   * @return the vote
   */
  VoteCommentDto deleteCommentVote(Long id);
}
