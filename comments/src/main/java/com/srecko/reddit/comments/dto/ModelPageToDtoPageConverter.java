package com.srecko.reddit.comments.dto;

import com.srecko.reddit.comments.entity.Comment;
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
   * Convert comments page.
   *
   * @param pageable    the pageable
   * @param page        the page
   * @param modelMapper the model mapper
   * @return the page
   */
  public static PageImpl<CommentDto> convertComments(Pageable pageable, Page<Comment> page,
      ModelMapper modelMapper) {
    logger.info("Converting Comments to CommentDtos");
    return convert(pageable, page, modelMapper);
  }

  private static <E, D> PageImpl<D> convert(Pageable pageable, Page<E> page,
      ModelMapper modelMapper) {
    List<D> objects = page.getContent()
        .stream()
        .map(obj -> modelMapper.map(obj, (Class<D>) CommentDto.class))
        .toList();
    return new PageImpl<>(objects, pageable, objects.size());
  }
}
