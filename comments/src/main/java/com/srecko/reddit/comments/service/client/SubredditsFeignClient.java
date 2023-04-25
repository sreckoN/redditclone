package com.srecko.reddit.comments.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Subreddits feign client.
 */
@FeignClient("subreddits")
public interface SubredditsFeignClient {

  /**
   * Check if subreddit exists.
   *
   * @param subredditId the subreddit id
   */
  @RequestMapping(method = RequestMethod.HEAD, value = "/api/subreddits/checkIfSubredditExists",
      consumes = "application/json")
  void checkIfSubredditExists(@RequestBody Long subredditId);
}
