package com.srecko.reddit.users.service;

import com.srecko.reddit.users.assembler.PageRequestAssembler;
import com.srecko.reddit.users.dto.UserDto;
import com.srecko.reddit.users.dto.UserPageMapper;
import com.srecko.reddit.users.entity.User;
import com.srecko.reddit.users.exception.UserNotFoundException;
import com.srecko.reddit.users.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type User service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

  /**
   * Instantiates a new User detail service.
   *
   * @param userRepository the user repository
   * @param modelMapper    the model mapper
   */
  @Autowired
  public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  /*@Override
  public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    User user = userOptional
        .orElseThrow(() -> new UserNotFoundException(username));
    if (!user.isEnabled()) {
      throw new AccountDisabledException(username);
    }
    return new UserMediator(user);
  }*/

  /*private Collection<? extends GrantedAuthority> getAuthorities(String role) {
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }*/

  @Override
  public Page<UserDto> getUsers(Pageable pageable) {
    logger.info("Getting all users");
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("username"),
        Sort.by(Direction.ASC, "username"));
    Page<User> users = userRepository.findAll(pageRequest);
    return UserPageMapper.convertUsers(pageable, users, modelMapper);
  }

  @Override
  public UserDto getUser(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new UserNotFoundException(userId);
    }
    return modelMapper.map(userOptional.get(), UserDto.class);
  }

  @Override
  public UserDto getUserByUsername(String username) {
    return modelMapper.map(getUserByUsernameInternal(username), UserDto.class);
  }

  @Override
  public User getUserByUsernameInternal(String username) {
    logger.info("Getting by username: {}", username);
    Optional<User> user = userRepository.findUserByUsername(username);
    if (user.isEmpty()) {
      throw new UserNotFoundException(username);
    }
    return user.get();
  }

  @Override
  public UserDto deleteUser(String username) {
    logger.info("Deleting user: {}", username);
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    if (userOptional.isPresent()) {
      userRepository.delete(userOptional.get());
      return modelMapper.map(userOptional.get(), UserDto.class);
    } else {
      throw new UserNotFoundException(username);
    }
  }

  @Override
  public boolean existsUserByEmail(String email) {
    logger.info("Checking if exists user by email: {}", email);
    return userRepository.existsUserByEmail(email);
  }

  @Override
  public boolean existsUserByUsername(String username) {
    logger.info("Checking if exists user by username: {}", username);
    return userRepository.existsUserByUsername(username);
  }

  @Override
  public UserDto save(User user) {
    logger.info("Saving user into database: {}", user.getUsername());
    User savedUser = userRepository.save(user);
    return modelMapper.map(savedUser, UserDto.class);
  }

  @Override
  public void deleteUnverifiedUsers() {
    /*List<EmailVerificationToken> expiredTokens =
        emailVerificationRepository.findAllByExpiryDateBefore(new Date());
    for (EmailVerificationToken expiredToken : expiredTokens) {
      userRepository.delete(expiredToken.getUser());
    }
    emailVerificationRepository.deleteAll(expiredTokens);*/
  }

  @Override
  public Long getUserIdByUsername(String username) {
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    if (userOptional.isEmpty()) {
      throw new UserNotFoundException(username);
    }
    return userOptional.get().getId();
  }

  @Override
  public PageImpl<UserDto> search(String q, Pageable pageable) {
    logger.info("Searching for usernames that match query: {}", q);
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, List.of("username"),
        Sort.by(Direction.ASC, "username"));
    Page<User> users = userRepository.findByUsernameContainingIgnoreCase(q,
        pageRequest);
    return UserPageMapper.convertUsers(pageable, users, modelMapper);
  }

  @Override
  public void checkIfExists(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new UserNotFoundException(userId);
    }
  }
}