package com.srecko.reddit.service;

import jakarta.mail.MessagingException;
import java.util.Map;

/**
 * The interface Email service.
 *
 * @author Srecko Nikolic
 */
public interface EmailService {

  /**
   * Send email.
   *
   * @param address the address
   * @param title   the title
   * @param message the message
   */
  void sendEmail(String address, String title, String message);

  /**
   * Send verification email.
   *
   * @param address         the address
   * @param confirmationUrl the confirmation url
   */
  void sendVerificationEmail(String address, String confirmationUrl);

  /**
   * Send html email.
   *
   * @param address  the address
   * @param title    the title
   * @param htmlBody the html body
   * @throws MessagingException the messaging exception
   */
  void sendHtmlEmail(String address, String title, String htmlBody) throws MessagingException;

  /**
   * Send message using thymeleaf template.
   *
   * @param address the address
   * @param subject the subject
   * @param model   the model
   * @throws MessagingException the messaging exception
   */
  void sendMessageUsingThymeleafTemplate(String address, String subject, Map<String, Object> model)
      throws MessagingException;
}