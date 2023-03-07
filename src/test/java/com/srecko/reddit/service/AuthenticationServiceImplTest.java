package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.*;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.repository.EmailVerificationRepository;
import com.srecko.reddit.repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
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
    private UserService userService;

    @MockBean
    private EmailVerificationRepository emailVerificationRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private RefreshTokenService refreshTokenService;

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
    void testRegisterThrowsEmailAlreadyInUse() {
        // given
        given(userService.existsUserByEmail(any())).willReturn(true);

        // when then
        assertThrows(EmailAlreadyInUseException.class, () -> authenticationServiceImpl
                .register(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB"), ""));
    }

    @Test
    void testRegisterThrowsUsernameNotAvailableException() {
        // given
        given(userService.existsUserByEmail(any())).willReturn(false);
        given(userService.existsUserByUsername(any())).willReturn(true);

        // when
        assertThrows(UsernameNotAvailableException.class, () -> authenticationServiceImpl
                .register(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB"), ""));
    }

    @Test
    void testRegisterThrowsRegistrationRequestNullException() {
        // given
        when(userService.existsUserByEmail(any())).thenReturn(true);
        when(userService.existsUserByUsername(any())).thenReturn(true);
        when(userService.save(any())).thenReturn(user);

        // when then
        assertThrows(RegistrationRequestNullException.class, () -> {
            authenticationServiceImpl.register(null, "");
        });
    }

    @Test
    void testRegister() {
        // given
        given(userService.existsUserByEmail(any())).willReturn(false);
        given(userService.existsUserByUsername(any())).willReturn(false);

        // when
        authenticationServiceImpl.register(new RegistrationRequest(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getUsername(), user.getPassword(), user.getCountry()), "");

        // then
        verify(userService).save(any());
    }

    @Test
    void generateEmailVerificationToken() {
        // given
        // when
        authenticationServiceImpl.generateEmailVerificationToken(user, "http://localhost:8080/api/auth");
        // then
    }

    @Test
    void saveEmailVerificationToken() {
        // given
        EmailVerificationToken token = new EmailVerificationToken();
        given(emailVerificationRepository.save(token)).willReturn(token);

        // when
        authenticationServiceImpl.saveEmailVerificationToken(token);
        // then
    }

    @Test
    void getVerificationToken() throws ParseException {
        // given
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
        String dateString = "2025-02-15";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        token.setExpiryDate(format.parse(dateString));
        given(emailVerificationRepository.getEmailVerificationTokenByToken(token.getToken())).willReturn(Optional.of(token));

        // when
        EmailVerificationToken returnedToken = authenticationServiceImpl.getVerificationToken(token.getToken());

        // then
        assertEquals(token.getToken(), returnedToken.getToken());
    }

    @Test
    void getVerificationTokenThrowsEmailVerificationTokenNotFoundException() throws ParseException {
        // given
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
        given(emailVerificationRepository.getEmailVerificationTokenByToken(token.getToken())).willReturn(Optional.empty());

        // when then
        assertThrows(EmailVerificationTokenNotFoundException.class, () -> {
            authenticationServiceImpl.getVerificationToken(token.getToken());
        });
    }

    @Test
    void getVerificationTokenThrowsInvalidEmailVerificationTokenException() throws ParseException {
        // given
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
        given(emailVerificationRepository.getEmailVerificationTokenByToken(any())).willReturn(Optional.of(token));

        // when then
        assertThrows(InvalidEmailVerificationTokenException.class, () -> {
            authenticationServiceImpl.getVerificationToken("ea9b3023-f4b8-45f4-8e5a-4e888");
        });
    }

    @Test
    void getVerificationTokenThrowsEmailVerificationTokenExpiredException() throws ParseException {
        // given
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
        String dateString = "1990-02-15";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        token.setExpiryDate(format.parse(dateString));
        given(emailVerificationRepository.getEmailVerificationTokenByToken(token.getToken())).willReturn(Optional.of(token));

        // when then
        assertThrows(EmailVerificationTokenExpiredException.class, () -> {
            authenticationServiceImpl.getVerificationToken(token.getToken());
        });
    }

    @Test
    void enableUserAccount() {
        //given
        given(userService.save(any())).willReturn(null);

        // when then
        authenticationServiceImpl.enableUserAccount(user);
    }
}