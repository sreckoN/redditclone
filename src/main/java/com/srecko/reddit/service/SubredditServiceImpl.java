package com.srecko.reddit.service;

import com.srecko.reddit.assembler.PageRequestAssembler;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.dto.requests.SubredditRequest;
import com.srecko.reddit.dto.util.ModelPageToDtoPageConverter;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Subreddit service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {UserNotFoundException.class, SubredditNotFoundException.class})
public class SubredditServiceImpl implements SubredditService {

  private final SubredditRepository subredditRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(SubredditServiceImpl.class);

  /**
   * Instantiates a new Subreddit service.
   *
   * @param subredditRepository the subreddit repository
   * @param userRepository      the user repository
   * @param modelMapper         the model mapper
   */
  @Autowired
  public SubredditServiceImpl(SubredditRepository subredditRepository,
      UserRepository userRepository, ModelMapper modelMapper) {
    this.subredditRepository = subredditRepository;
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public Page<SubredditDto> getAll(Pageable pageable) {
    logger.info("Getting all subreddits");
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("name"),
        Sort.by(Direction.ASC, "name"));
    Page<Subreddit> subreddits = subredditRepository.findAll(pageRequest);
    return ModelPageToDtoPageConverter.convertSubreddits(pageable, subreddits, modelMapper);
  }

  @Override
  public SubredditDto getSubredditById(Long id) {
    logger.info("Getting subreddit: {}", id);
    Optional<Subreddit> subredditOptional = subredditRepository.findById(id);
    if (subredditOptional.isPresent()) {
      return modelMapper.map(subredditOptional.get(), SubredditDto.class);
    } else {
      throw new SubredditNotFoundException(id);
    }
  }

  @Override
  public SubredditDto save(SubredditRequest subredditRequest) {
    logger.info("Saving subreddit into database");
    UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Optional<User> user = userRepository.findUserByUsername(userMediator.getUsername());
    if (user.isPresent()) {
      Subreddit subreddit =
          new Subreddit(subredditRequest.getName(), subredditRequest.getDescription(), user.get());
      subreddit = subredditRepository.save(subreddit);
      return modelMapper.map(subreddit, SubredditDto.class);
    } else {
      throw new UserNotFoundException(userMediator.getUsername());
    }
  }

  @Override
  public SubredditDto delete(Long id) {
    logger.info("Deleting subreddit: {}", id);
    Optional<Subreddit> subredditOptional = subredditRepository.findById(id);
    if (subredditOptional.isPresent()) {
      subredditRepository.delete(subredditOptional.get());
      return modelMapper.map(subredditOptional.get(), SubredditDto.class);
    } else {
      throw new SubredditNotFoundException(id);
    }
  }

  @Override
  public SubredditDto update(SubredditRequest subredditRequest) {
    logger.info("Updating subreddit: {}", subredditRequest.getSubredditId());
    Optional<Subreddit> subredditOptional = subredditRepository.findById(
        subredditRequest.getSubredditId());
    if (subredditOptional.isPresent()) {
      Subreddit subreddit = subredditOptional.get();
      subreddit.setName(subredditRequest.getName());
      subreddit.setDescription(subredditRequest.getDescription());
      subreddit = subredditRepository.save(subreddit);
      return modelMapper.map(subreddit, SubredditDto.class);
    } else {
      throw new SubredditNotFoundException(subredditRequest.getSubredditId());
    }
  }
}
