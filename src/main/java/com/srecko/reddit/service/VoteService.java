package com.srecko.reddit.service;

import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Vote;

public interface VoteService {

  Vote saveCommentVote(VoteCommentDto voteDto);

  Vote savePostVote(VotePostDto voteDto);

  Vote deletePostVote(Long id);

  Vote deleteCommentVote(Long id);
}
