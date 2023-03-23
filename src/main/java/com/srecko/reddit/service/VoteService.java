package com.srecko.reddit.service;

import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Vote;

/**
 * The interface Vote service.
 *
 * @author Srecko Nikolic
 */
public interface VoteService {

  /**
   * Save comment vote vote.
   *
   * @param voteDto the vote dto
   * @return the vote
   */
  Vote saveCommentVote(VoteCommentDto voteDto);

  /**
   * Save post vote vote.
   *
   * @param voteDto the vote dto
   * @return the vote
   */
  Vote savePostVote(VotePostDto voteDto);

  /**
   * Delete post vote vote.
   *
   * @param id the id
   * @return the vote
   */
  Vote deletePostVote(Long id);

  /**
   * Delete comment vote vote.
   *
   * @param id the id
   * @return the vote
   */
  Vote deleteCommentVote(Long id);
}
