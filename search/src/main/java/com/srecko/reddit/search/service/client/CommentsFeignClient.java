package com.srecko.reddit.search.service.client;

import com.srecko.reddit.search.config.AppConfiguration;
import com.srecko.reddit.search.dto.CommentDto;
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
 * The interface Comments feign client.
 */
@FeignClient(value = "comments", configuration = AppConfiguration.class)
public interface CommentsFeignClient {

  /**
   * Search comments paged model.
   *
   * @param query    the query
   * @param pageable the pageable
   * @return the paged model
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/comments/search",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  PagedModel<EntityModel<CommentDto>> searchComments(
      @RequestBody String query,
      @SpringQueryMap Pageable pageable);
}
