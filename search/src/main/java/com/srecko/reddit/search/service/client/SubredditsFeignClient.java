package com.srecko.reddit.search.service.client;

import com.srecko.reddit.search.config.AppConfiguration;
import com.srecko.reddit.search.dto.SubredditDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Subreddits feign client.
 */
@FeignClient(value = "subreddits", configuration = AppConfiguration.class)
public interface SubredditsFeignClient {

  /**
   * Search subreddits paged model.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the paged model
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/subreddits/search",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  PagedModel<EntityModel<SubredditDto>> searchSubreddits(
      @RequestBody String query,
      @SpringQueryMap Pageable pageable);
}
