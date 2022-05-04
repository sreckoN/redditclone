package com.srecko.reddit.service;

import com.srecko.reddit.dto.RegistrationRequest;

public interface AuthenticationService {

    void saveUser(RegistrationRequest registrationRequest);
}
