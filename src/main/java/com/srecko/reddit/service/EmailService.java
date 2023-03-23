package com.srecko.reddit.service;

import jakarta.mail.MessagingException;
import java.util.Map;

public interface EmailService {

  void sendEmail(String address, String title, String message);

  void sendVerificationEmail(String address, String confirmationUrl);

  void sendHtmlEmail(String address, String title, String htmlBody) throws MessagingException;

  void sendMessageUsingThymeleafTemplate(String address, String subject, Map<String, Object> model)
      throws MessagingException;
}