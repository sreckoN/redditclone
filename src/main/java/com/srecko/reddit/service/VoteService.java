package com.srecko.reddit.service;

import com.srecko.reddit.dto.VoteDto;
import com.srecko.reddit.entity.Vote;

public interface VoteService {

    Vote save(VoteDto voteDto);
    Vote delete(Long id);
}
