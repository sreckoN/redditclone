package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.EmailAlreadyInUseException;
import com.srecko.reddit.exception.RegistrationRequestNullException;
import com.srecko.reddit.exception.UsernameNotAvailableException;
import com.srecko.reddit.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceImplTest {

    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setComments(new ArrayList<>());
        user.setCountry("GB");
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(123L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setPosts(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        user.setUsername("janedoe");
    }

    @Test
    void testSaveUserThrowsEmailAlreadyInUse() {
        // given
        given(userRepository.existsUserByEmail(any())).willReturn(true);

        // when
        assertThrows(EmailAlreadyInUseException.class, () -> authenticationServiceImpl
                .saveUser(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB")));

        // then
        verify(userRepository).existsUserByEmail(any());
    }

    @Test
    void testSaveUserThrowsUsernameNotAvailableException() {
        // given
        given(userRepository.existsUserByEmail(any())).willReturn(false);
        given(userRepository.existsUserByUsername(any())).willReturn(true);

        // when
        assertThrows(UsernameNotAvailableException.class, () -> authenticationServiceImpl
                .saveUser(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB")));

        // then
        verify(userRepository).existsUserByEmail(any());
        verify(userRepository).existsUserByUsername(any());
    }

    @Test
    void testSaveUserThrowsRegistrationRequestNullException() {
        // given
        when(userRepository.existsUserByEmail((String) any())).thenReturn(true);
        when(userRepository.existsUserByUsername((String) any())).thenReturn(true);
        when(userRepository.save((User) any())).thenReturn(user);

        // when then
        assertThrows(RegistrationRequestNullException.class, () -> {
            authenticationServiceImpl.saveUser(null);
        });
    }

    @Test
    void testSaveUser() {
        // given
        given(userRepository.existsUserByEmail(any())).willReturn(false);
        given(userRepository.existsUserByUsername(any())).willReturn(false);

        // when
        authenticationServiceImpl.saveUser(new RegistrationRequest(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getUsername(), user.getPassword(), user.getCountry()));

        // then
        verify(userRepository).save(any());
    }
}