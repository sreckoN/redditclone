package com.srecko.reddit.dto.util;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
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

  /**
   * Convert subreddits page.
   *
   * @param pageable    the pageable
   * @param page        the page
   * @param modelMapper the model mapper
   * @return the page
   */
  public static PageImpl<SubredditDto> convertSubreddits(Pageable pageable, Page<Subreddit> page,
      ModelMapper modelMapper) {
    logger.info("Converting Subreddits to SubredditDtos");
    return convert(pageable, page, SubredditDto.class, modelMapper);
  }

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
    return convert(pageable, page, PostDto.class, modelMapper);
  }

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
    return convert(pageable, page, CommentDto.class, modelMapper);
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
