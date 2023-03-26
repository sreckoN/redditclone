package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.authentication.AccountDisabledException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.EmailVerificationRepository;
import com.srecko.reddit.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserDetailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailServiceImplTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private EmailVerificationRepository emailVerificationRepository;

  @Autowired
  private UserDetailServiceImpl userService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    user.setId(123L);
    user.setComments(new ArrayList<>());
    user.setPosts(new ArrayList<>());
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
  }

  @Test
  void loadUserByUsername_ReturnsUserDetailsOfUser_WhenSuccessfullyLoaded() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    // when
    UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

    // then
    assertNotNull(userDetails);
    assertEquals(user.getUsername(), userDetails.getUsername());
    assertEquals(user.isEnabled(), userDetails.isEnabled());
  }

  @Test
  void loadUserByUsername_ThrowsUserNotFoundExceptionException_WhenUserDoesNotExist() {
    // given when then
    assertThrows(UserNotFoundException.class, () -> {
      userService.loadUserByUsername(user.getUsername());
    });
  }

  @Test
  void loadUserByUsername_ThrowsAccountNotEnabledException_WhenUserAccountIsNotEnabler() {
    // given
    user.setEnabled(false);
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    // when then
    assertThrows(AccountDisabledException.class, () -> {
      userService.loadUserByUsername(user.getUsername());
    });
  }

  @Test
  void getUsers_ReturnsListOfAllUsers() {
    // given
    given(userRepository.findAll()).willReturn(List.of(user));

    // when
    List<User> users = userService.getUsers();

    // then
    assertNotNull(users);
    assertEquals(1, users.size());
    assertTrue(users.contains(user));
  }

  @Test
  void deleteUser_ReturnsDeletedUser_WhenSuccessfullyDeleted() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    // when
    User deleted = userService.deleteUser(user.getUsername());

    // then
    assertNotNull(deleted);
    assertEquals(user.getUsername(), deleted.getUsername());
    assertEquals(user.getFirstName(), deleted.getFirstName());
    assertEquals(user.getLastName(), deleted.getLastName());
    assertEquals(user.getEmail(), deleted.getEmail());
    assertEquals(user.isEnabled(), deleted.isEnabled());
  }

  @Test
  void deleteUser_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
    // given when then
    assertThrows(UserNotFoundException.class, () -> {
      userService.deleteUser(user.getUsername());
    });
  }

  @Test
  void existsUserByEmail_ReturnsTrue_WhenUSerExists() {
    // given
    given(userRepository.existsUserByEmail(user.getEmail())).willReturn(true);

    // when
    boolean result = userService.existsUserByEmail(user.getEmail());

    // then
    assertTrue(result);
  }

  @Test
  void existsUserByEmail_ReturnsFalse_WhenUserDoesNotExists() {
    // given
    given(userRepository.existsUserByEmail(user.getEmail())).willReturn(false);

    // when
    boolean result = userService.existsUserByEmail(user.getEmail());

    // then
    assertFalse(result);
  }

  @Test
  void existsUserByUsername_ReturnsTrue_WhenUserExists() {
    // given
    given(userRepository.existsUserByUsername(user.getUsername())).willReturn(true);

    // when
    boolean result = userService.existsUserByUsername(user.getUsername());

    // then
    assertTrue(result);
  }

  @Test
  void existsUserByUsername_ReturnsFalse_WhenUserDoesNotExists() {
    // given
    given(userRepository.existsUserByUsername(user.getUsername())).willReturn(false);

    // when
    boolean result = userService.existsUserByUsername(user.getUsername());

    // then
    assertFalse(result);
  }

  @Test
  void save_ReturnsSavedUser_WhenSuccessfullySaved() {
    // given
    given(userRepository.save(user)).willReturn(user);

    // when
    User savedUser = userService.save(user);

    // then
    assertEquals(user.getUsername(), savedUser.getUsername());
  }

  @Test
  void deleteUnverifiedUsers_DeletesUnverifiedUsers() {
    // given
    EmailVerificationToken token = new EmailVerificationToken();
    given(emailVerificationRepository.findAllByExpiryDateBefore(any())).willReturn(List.of(token));

    // when
    userService.deleteUnverifiedUsers();

    // then
    verify(emailVerificationRepository).findAllByExpiryDateBefore(any());
    verify(userRepository).delete(any());
    verify(emailVerificationRepository).deleteAll(any());
  }
}