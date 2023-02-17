package com.srecko.reddit.service;

import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;

public interface AuthenticationService {

    User saveUser(RegistrationRequest registrationRequest);
    void saveEmailVerificationToken(EmailVerificationToken token);
    void generateEmailVerificationToken(User user, String appUrl);
    EmailVerificationToken getVerificationToken(String token);
    void enableUserAccount(User user);
}
