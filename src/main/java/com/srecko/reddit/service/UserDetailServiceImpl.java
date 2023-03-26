package com.srecko.reddit.service;

import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.authentication.AccountDisabledException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.EmailVerificationRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type User detail service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional
public class UserDetailServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;
  private final EmailVerificationRepository emailVerificationRepository;

  /**
   * Instantiates a new User detail service.
   *
   * @param userRepository              the user repository
   * @param emailVerificationRepository the email verification repository
   */
  @Autowired
  public UserDetailServiceImpl(UserRepository userRepository,
      EmailVerificationRepository emailVerificationRepository) {
    this.userRepository = userRepository;
    this.emailVerificationRepository = emailVerificationRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    User user = userOptional
        .orElseThrow(() -> new UserNotFoundException(username));
    if (!user.isEnabled()) {
      throw new AccountDisabledException(username);
    }
    return new UserMediator(user);
  }

  private Collection<? extends GrantedAuthority> getAuthorities(String role) {
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }

  @Override
  public List<User> getUsers() {
    return (List<User>) userRepository.findAll();
  }

  @Override
  public User getUserByUsername(String username) {
    Optional<User> user = userRepository.findUserByUsername(username);
    if (user.isEmpty()) {
      throw new UserNotFoundException(username);
    }
    return user.get();
  }

  @Override
  public User getUserByEmail(String email) {
    Optional<User> userByEmail = userRepository.findUserByEmail(email);
    if (userByEmail.isEmpty()) {
      throw new UserNotFoundException(email);
    }
    return userByEmail.get();
  }

  @Override
  public User deleteUser(String username) {
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    if (userOptional.isPresent()) {
      userRepository.delete(userOptional.get());
      return userOptional.get();
    } else {
      throw new UserNotFoundException(username);
    }
  }

  @Override
  public boolean existsUserByEmail(String email) {
    return userRepository.existsUserByEmail(email);
  }

  @Override
  public boolean existsUserByUsername(String username) {
    return userRepository.existsUserByUsername(username);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public void deleteUnverifiedUsers() {
    List<EmailVerificationToken> expiredTokens =
        emailVerificationRepository.findAllByExpiryDateBefore(new Date());
    for (EmailVerificationToken expiredToken : expiredTokens) {
      userRepository.delete(expiredToken.getUser());
    }
    emailVerificationRepository.deleteAll(expiredTokens);
  }
}