package com.srecko.reddit.service;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import java.util.List;

/**
 * The interface Subreddit service.
 *
 * @author Srecko Nikolic
 */
public interface SubredditService {

  /**
   * Gets all.
   *
   * @return the all
   */
  List<Subreddit> getAll();

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
