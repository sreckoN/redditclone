package com.srecko.reddit.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

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
    public void sendVerificationEmail(String address, String confirmationUrl) {
        String message = "Click on the button to verify your email address." + "\r\n" + "http://localhost:8080/api/auth" + confirmationUrl;
        sendEmail(address, "Verify your email address", message);
    }

    @Override
    public void sendHtmlEmail(String address, String title, String htmlBody) throws MessagingException {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");
        helper.setTo(address);
        helper.setSubject(title);
        helper.setText(htmlBody, true);
        mailSender.send(email);
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String address, String subject, Map<String, Object> model) throws MessagingException {
        Context context = new Context();
        context.setVariables(model);
        String html = thymeleafTemplateEngine.process("verification_email_template.html", context);
        sendHtmlEmail(address, subject, html);
    }
}