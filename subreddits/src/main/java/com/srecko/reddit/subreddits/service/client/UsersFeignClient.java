package com.srecko.reddit.subreddits.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("users")
public interface UsersFeignClient {

  @RequestMapping(method = RequestMethod.GET, value = "/api/users/getUserIdByUsername",
      consumes = "application/json")
  Long getUserId(@RequestBody String username);
}
