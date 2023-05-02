package com.srecko.reddit.users.dto;

import com.srecko.reddit.users.entity.User;
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
   * Convert users page.
   *
   * @param pageable    the pageable
   * @param page        the page
   * @param modelMapper the model mapper
   * @return the page
   */
  public static PageImpl<UserDto> convertUsers(Pageable pageable, Page<User> page,
      ModelMapper modelMapper) {
    logger.info("Converting Users to UserDtos");
    return convert(pageable, page, UserDto.class, modelMapper);
  }

  private static <E, D> PageImpl<D> convert(Pageable pageable, Page<E> page, Class<D> dtoClass,
      ModelMapper modelMapper) {
    List<D> objects = page.getContent()
        .stream()
        .map(obj -> modelMapper.map(obj, dtoClass))
        .toList();
    return new PageImpl<>(objects, pageable, objects.size());
  }
}
