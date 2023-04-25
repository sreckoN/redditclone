package com.srecko.reddit.votes.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Post feign client.
 */
@FeignClient("comments")
public interface CommentsFeignClient {

  /**
   * Check if post exists.
   *
   * @param postId the post id
   */
  @RequestMapping(method = RequestMethod.HEAD, value = "/api/comments/checkIfExists",
      consumes = "application/json")
  void checkIfCommentExists(@RequestBody Long postId);
}
