package com.srecko.reddit.comments.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Users feign client.
 */
@FeignClient("users")
public interface UsersFeignClient {

  /**
   * Gets user id.
   *
   * @param username the username
   * @return the user id
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/users/getUserIdByUsername",
      consumes = "application/json")
  Long getUserId(@RequestBody String username);

  /**
   * Check if exists.
   *
   * @param userId the user id
   */
  @RequestMapping(method = RequestMethod.HEAD, value = "/api/users/checkIfExists",
      consumes = "application/json")
  void checkIfExists(@RequestBody Long userId);
}
