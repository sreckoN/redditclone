package com.srecko.reddit.posts.dto;

import com.srecko.reddit.posts.entity.Post;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * The type Model page to dto page converter.
 *
 * @author Srecko Nikolic
 */
@Component
public class ModelPageToDtoPageConverter {

  private static final Logger logger = LogManager.getLogger(ModelPageToDtoPageConverter.class);

  /**
   * Convert posts page.
   *
   * @param pageable    the pageable
   * @param page        the page
   * @param modelMapper the model mapper
   * @return the page
   */
  public static PageImpl<PostDto> convertPosts(Pageable pageable, Page<Post> page,
      ModelMapper modelMapper) {
    logger.info("Converting Posts to PostDtos");
    return convert(pageable, page, modelMapper);
  }

  private static <E, D> PageImpl<D> convert(Pageable pageable, Page<E> page,
      ModelMapper modelMapper) {
    List<D> objects = page.getContent()
        .stream()
        .map(obj -> modelMapper.map(obj, (Class<D>) PostDto.class))
        .toList();
    return new PageImpl<>(objects, pageable, objects.size());
  }
}
