package com.srecko.reddit.search.service.client;

import com.srecko.reddit.search.config.AppConfiguration;
import com.srecko.reddit.search.dto.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Posts feign client.
 *
 * @author Srecko Nikolic
 */
@FeignClient(value = "posts", configuration = AppConfiguration.class)
public interface PostsFeignClient {

  /**
   * Search posts paged model.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the paged model
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/posts/search",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  PagedModel<EntityModel<PostDto>> searchPosts(
      @RequestBody String query,
      @SpringQueryMap Pageable pageable);

  /**
   * Search posts in subreddit paged model.
   *
   * @param subredditId the subreddit id
   * @param query       the query
   * @param pageable    the pageable
   * @return the paged model
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/posts/search/subreddit/{subredditId}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  PagedModel<EntityModel<PostDto>> searchPostsInSubreddit(
      @PathVariable("subredditId") Long subredditId,
      @RequestBody String query,
      @SpringQueryMap Pageable pageable);
}
