package com.srecko.reddit.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * The type Email service.
 *
 * @author Srecko Nikolic
 */
@Service
public class EmailServiceImpl implements EmailService {

  private Environment env;

  private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private SpringTemplateEngine thymeleafTemplateEngine;

  /**
   * Instantiates a new Email service.
   *
   * @param env the env
   */
  @Autowired
  public EmailServiceImpl(Environment env) {
    this.env = env;
  }

  @Override
  public void sendEmail(String address, String title, String message) {
    SimpleMailMessage email = new SimpleMailMessage();
    email.setFrom(env.getProperty("spring.mail.username"));
    email.setTo(address);
    email.setSubject(title);
    email.setText(message);
    mailSender.send(email);
  }

  @Override
  public void sendHtmlEmail(String address, String title, String htmlBody)
      throws MessagingException {
    MimeMessage email = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");
    helper.setTo(address);
    helper.setSubject(title);
    helper.setText(htmlBody, true);
    mailSender.send(email);
  }

  @Override
  public void sendMessageUsingThymeleafTemplate(String address, String subject,
      Map<String, Object> model) throws MessagingException {
    Context context = new Context();
    context.setVariables(model);
    String template = "verification_email_template.html";
    String html = thymeleafTemplateEngine.process(template, context);
    logger.info("Sending email using template: {}", template);
    sendHtmlEmail(address, subject, html);
  }
}