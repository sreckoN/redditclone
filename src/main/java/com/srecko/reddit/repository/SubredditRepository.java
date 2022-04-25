package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Subreddit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends PagingAndSortingRepository<Subreddit, Long> {
}