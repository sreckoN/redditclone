package com.srecko.reddit.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.srecko.reddit.users.dto.UserDto;
import com.srecko.reddit.users.entity.User;
import com.srecko.reddit.users.exception.UserNotFoundException;
import com.srecko.reddit.users.repository.UserRepository;
import com.srecko.reddit.users.service.utils.TestConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private UserServiceImpl userService;

  private User user;

  private final ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  void setUp() {
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    user.setId(123L);
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
  }

  /*@Test
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
    assertThrows(UserNotFoundException.class, () ->
        userService.loadUserByUsername(user.getUsername()));
  }

  @Test
  void loadUserByUsername_ThrowsAccountNotEnabledException_WhenUserAccountIsNotEnabler() {
    // given
    user.setEnabled(false);
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    // when then
    assertThrows(AccountDisabledException.class, () ->
        userService.loadUserByUsername(user.getUsername()));
  }*/

  @Test
  void getUsers_ReturnsListOfAllUsers() {
    // given
    given(userRepository.findAll(any(PageRequest.class))).willReturn(new PageImpl<>(List.of(user)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));

    // when
    Page<UserDto> page = userService.getUsers(pageRequest);

    // then
    assertNotNull(page);
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(user, UserDto.class)));
  }

  @Test
  void deleteUser_ReturnsDeletedUser_WhenSuccessfullyDeleted() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    // when
    UserDto deleted = userService.deleteUser(user.getUsername());

    // then
    assertNotNull(deleted);
    assertEquals(user.getId(), deleted.getId());
    assertEquals(user.getUsername(), deleted.getUsername());
    assertEquals(user.getCountry(), deleted.getCountry());
    assertEquals(user.getRegistrationDate(), deleted.getRegistrationDate());
  }

  @Test
  void deleteUser_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
    // given when then
    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getUsername()));
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
    UserDto savedUser = userService.save(user);

    // then
    assertNotNull(savedUser);
    assertEquals(user.getUsername(), savedUser.getUsername());
  }
}