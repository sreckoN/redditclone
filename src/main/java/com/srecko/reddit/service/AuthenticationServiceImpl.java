package com.srecko.reddit.service;

import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.EmailAlreadyInUseException;
import com.srecko.reddit.exception.RegistrationRequestNullException;
import com.srecko.reddit.exception.UsernameNotAvailableException;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationRequest registrationRequest) {
        if (registrationRequest == null) {
            throw new RegistrationRequestNullException();
        } else if (userRepository.existsUserByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyInUseException(registrationRequest.getEmail());
        } else if (userRepository.existsUserByUsername(registrationRequest.getUsername())) {
            throw new UsernameNotAvailableException(registrationRequest.getUsername());
        } else {
            User user = new User(registrationRequest.getFirstName(),
                    registrationRequest.getLastName(),
                    registrationRequest.getEmail(),
                    registrationRequest.getUsername(),
                    passwordEncoder.encode(registrationRequest.getPassword()),
                    registrationRequest.getCountry(),
                    true);
            userRepository.save(user);
        }
    }
}
