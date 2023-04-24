package com.srecko.reddit.users.service.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * The interface Subreddit feign client.
 *
 * @author Srecko Nikolic
 */
@FeignClient("subreddits")
public interface SubredditFeignClient {


}
