package com.srecko.reddit.subreddits.service;

import com.srecko.reddit.subreddits.assembler.PageRequestAssembler;
import com.srecko.reddit.subreddits.dto.SubredditDto;
import com.srecko.reddit.subreddits.dto.SubredditDtoMapper;
import com.srecko.reddit.subreddits.dto.SubredditRequest;
import com.srecko.reddit.subreddits.entity.Subreddit;
import com.srecko.reddit.subreddits.exception.SubredditNotFoundException;
import com.srecko.reddit.subreddits.repository.SubredditRepository;
import com.srecko.reddit.subreddits.service.client.UsersFeignClient;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Subreddit service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional(rollbackFor = {SubredditNotFoundException.class})
public class SubredditServiceImpl implements SubredditService {

  private final SubredditRepository subredditRepository;
  private final UsersFeignClient usersFeignClient;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(SubredditServiceImpl.class);

  /**
   * Instantiates a new Subreddit service.
   *
   * @param subredditRepository the subreddit repository
   * @param usersFeignClient    the users feign client
   * @param modelMapper         the model mapper
   */
  @Autowired
  public SubredditServiceImpl(SubredditRepository subredditRepository,
      UsersFeignClient usersFeignClient,
      ModelMapper modelMapper) {
    this.subredditRepository = subredditRepository;
    this.usersFeignClient = usersFeignClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public Page<SubredditDto> getAll(Pageable pageable) {
    logger.info("Getting all subreddits");
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("name"),
        Sort.by(Direction.ASC, "name"));
    Page<Subreddit> subreddits = subredditRepository.findAll(pageRequest);
    return SubredditDtoMapper.convertSubreddits(pageable, subreddits, modelMapper);
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
    /*UserMediator userMediator = (UserMediator) SecurityContextHolder.getContext()
    .getAuthentication().getPrincipal();
    Long userId = usersFeignClient.getUserId(userMediator.getUsername());*/
    Long userId = usersFeignClient.getUserId("username");
    Subreddit subreddit =
        new Subreddit(subredditRequest.getName(), subredditRequest.getDescription(), userId);
    subreddit = subredditRepository.save(subreddit);
    return modelMapper.map(subreddit, SubredditDto.class);
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

  @Override
  public void checkIfExists(Long subredditId) {
    Optional<Subreddit> subredditOptional = subredditRepository.findById(subredditId);
    if (subredditOptional.isEmpty()) {
      throw new SubredditNotFoundException(subredditId);
    }
  }

  @Override
  public Page<SubredditDto> search(String query, Pageable pageable) {
    logger.info("Searching for subreddits that match query: {}", query);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("name"),
        Sort.by(Direction.ASC, "name"));
    Page<Subreddit> users = subredditRepository.findByNameContainingIgnoreCase(query,
        pageRequest);
    return SubredditDtoMapper.convertSubreddits(pageable, users, modelMapper);
  }
}
