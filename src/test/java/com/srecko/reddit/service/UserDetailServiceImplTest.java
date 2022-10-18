package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {UserDetailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailServiceImplTest {

    @MockBean
    private UserRepository userRepository;

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
    void loadUserByUsername() {
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
    void loadUserByUsernameThrowsUsernameNotFoundException() {
        // given when then
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(user.getUsername());
        });
    }

    @Test
    void getUsers() {
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
    void getUser() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

        // when
        User userGotten = userService.getUser(user.getUsername());

        // then
        assertNotNull(userGotten);
        assertEquals(user.getUsername(), userGotten.getUsername());
        assertEquals(user.getFirstName(), userGotten.getFirstName());
        assertEquals(user.getLastName(), userGotten.getLastName());
        assertEquals(user.getEmail(), userGotten.getEmail());
        assertEquals(user.isEnabled(), userGotten.isEnabled());
    }

    @Test
    void getUserThrowsUserNotFoundException() {
        // given when then
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(user.getUsername());
        });
    }

    @Test
    void deleteUser() {
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
    void deleteUserThrowsUserNotFoundException() {
        // given when then
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(user.getUsername());
        });
    }
}