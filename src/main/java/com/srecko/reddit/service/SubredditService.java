package com.srecko.reddit.service;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Subreddit service.
 *
 * @author Srecko Nikolic
 */
public interface SubredditService {

  /**
   * Gets all.
   *
   * @param pageable the pageable
   * @return the all
   */
  Page<Subreddit> getAll(Pageable pageable);

  /**
   * Gets subreddit by id.
   *
   * @param id the id
   * @return the subreddit by id
   */
  Subreddit getSubredditById(Long id);

  /**
   * Save subreddit.
   *
   * @param subredditDto the subreddit dto
   * @return the subreddit
   */
  Subreddit save(SubredditDto subredditDto);

  /**
   * Delete subreddit.
   *
   * @param id the id
   * @return the subreddit
   */
  Subreddit delete(Long id);

  /**
   * Update subreddit.
   *
   * @param subredditDto the subreddit dto
   * @return the subreddit
   */
  Subreddit update(SubredditDto subredditDto);
}
