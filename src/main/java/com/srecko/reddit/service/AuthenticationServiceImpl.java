package com.srecko.reddit.service;

import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.*;
import com.srecko.reddit.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder, EmailVerificationRepository emailVerificationRepository, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationRepository = emailVerificationRepository;
        this.emailService = emailService;
    }

    @Override
    public User saveUser(RegistrationRequest registrationRequest) {
        if (registrationRequest == null) {
            throw new RegistrationRequestNullException();
        } else if (userService.existsUserByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyInUseException(registrationRequest.getEmail());
        } else if (userService.existsUserByUsername(registrationRequest.getUsername())) {
            throw new UsernameNotAvailableException(registrationRequest.getUsername());
        } else {
            User user = new User(registrationRequest.getFirstName(),
                    registrationRequest.getLastName(),
                    registrationRequest.getEmail(),
                    registrationRequest.getUsername(),
                    passwordEncoder.encode(registrationRequest.getPassword()),
                    registrationRequest.getCountry(),
                    false);
            return userService.save(user);
        }
    }
    
    @Override
    public void generateEmailVerificationToken(User user, String appUrl) {
        EmailVerificationToken token = new EmailVerificationToken(user);
        saveEmailVerificationToken(token);
        String recipientEmail = user.getEmail();
        String confirmationUrl = appUrl + token.getToken();
        Map<String, Object> vars = new HashMap<>();
        vars.put("recipientName", user.getFirstName());
        vars.put("confirmationUrl", confirmationUrl);
        try {
            emailService.sendMessageUsingThymeleafTemplate(recipientEmail, "Verify your email address", vars);
        } catch (MessagingException e) {
            throw new VerificationEmailSendingErrorException(e.getMessage());
        }
    }

    @Override
    public void saveEmailVerificationToken(EmailVerificationToken token) {
        emailVerificationRepository.save(token);
    }

    @Override
    public EmailVerificationToken getVerificationToken(String token) {
        Optional<EmailVerificationToken> emailVerificationTokenOptional = emailVerificationRepository.getEmailVerificationTokenByToken(token);
        if (emailVerificationTokenOptional.isEmpty()) {
            throw new EmailVerificationTokenNotFoundException();
        }
        EmailVerificationToken verificationToken = emailVerificationTokenOptional.get();
        if (!verificationToken.getToken().equals(token)) {
            throw new InvalidEmailVerificationTokenException();
        }
        if (isVerificationTokenExpired(verificationToken)) {
            throw new EmailVerificationTokenExpiredException();
        }
        return verificationToken;
    }

    @Override
    public void enableUserAccount(User user) {
        user.setEnabled(true);
        userService.save(user);
        emailVerificationRepository.deleteByUser_Id(user.getId());
    }

    private boolean isVerificationTokenExpired(EmailVerificationToken token) {
        Instant currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        return token.getExpiryDate().before(Date.from(currentTime));
    }
}
