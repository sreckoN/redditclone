package com.srecko.reddit.subreddits.service;

import com.srecko.reddit.subreddits.dto.SubredditDto;
import com.srecko.reddit.subreddits.dto.SubredditRequest;
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
  Page<SubredditDto> getAll(Pageable pageable);

  /**
   * Gets subreddit by id.
   *
   * @param id the id
   * @return the subreddit by id
   */
  SubredditDto getSubredditById(Long id);

  /**
   * Save subreddit.
   *
   * @param subredditRequest the subreddit dto
   * @return the subreddit
   */
  SubredditDto save(SubredditRequest subredditRequest);

  /**
   * Delete subreddit.
   *
   * @param id the id
   * @return the subreddit
   */
  SubredditDto delete(Long id);

  /**
   * Update subreddit.
   *
   * @param subredditRequest the subreddit dto
   * @return the subreddit
   */
  SubredditDto update(SubredditRequest subredditRequest);

  /**
   * Check if exists.
   *
   * @param subredditId the subreddit id
   */
  void checkIfExists(Long subredditId);

  /**
   * Search page.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the page
   */
  Page<SubredditDto> search(String query, Pageable pageable);
}
