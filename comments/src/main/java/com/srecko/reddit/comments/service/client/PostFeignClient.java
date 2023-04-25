package com.srecko.reddit.comments.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Post feign client.
 */
@FeignClient("posts")
public interface PostFeignClient {

  /**
   * Check if post exists.
   *
   * @param postId the post id
   */
  @RequestMapping(method = RequestMethod.HEAD, value = "/api/posts/checkIfExists",
      consumes = "application/json")
  void checkIfPostExists(@RequestBody Long postId);
}
