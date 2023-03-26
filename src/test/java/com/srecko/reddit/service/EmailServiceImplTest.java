package com.srecko.reddit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.spring6.SpringTemplateEngine;

@ContextConfiguration(classes = {EmailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {

  @MockBean
  private Environment env;

  @MockBean
  private JavaMailSender mailSender;

  @MockBean
  private SpringTemplateEngine thymeleafTemplateEngine;

  @Autowired
  private EmailServiceImpl emailService;

  @Test
  void sendEmail_SendsEmail() {
    // given
    given(env.getProperty("spring.mail.username")).willReturn("example@gmail.com");

    // when
    emailService.sendEmail("example@gmail.com", "Verify TEST", "Testing...");

    // then
    verify(mailSender).send(any(SimpleMailMessage.class));
  }

  @Test
  void sendVerificationEmail_SendsVerificationEmail() {
    // given when then
    emailService.sendVerificationEmail("example@gmail.com", "/api/auth/confirmRegistration");
  }
}