package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Subreddit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends PagingAndSortingRepository<Subreddit, Long> {

    Optional<Subreddit> findById(Long id);
}