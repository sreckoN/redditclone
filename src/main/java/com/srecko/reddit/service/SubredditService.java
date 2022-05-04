package com.srecko.reddit.service;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;

import java.util.List;

public interface SubredditService {

    List<Subreddit> getAll();
    Subreddit getSubredditById(Long id);
    Subreddit save(SubredditDto subredditDto);
    Subreddit delete(Long id);
    Subreddit update(SubredditDto subredditDto);
}
