package com.srecko.reddit.service;

import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;

public interface AuthenticationService {

    void register(RegistrationRequest request, String confirmationUrl);
    void saveEmailVerificationToken(EmailVerificationToken token);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void generateEmailVerificationToken(User user, String appUrl);
    EmailVerificationToken getVerificationToken(String token);
    void enableUserAccount(User user);
}
