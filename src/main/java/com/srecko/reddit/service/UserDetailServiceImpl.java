package com.srecko.reddit.service;

import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.AccountNotEnabledException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.EmailVerificationRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserDetailServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository, EmailVerificationRepository emailVerificationRepository) {
        this.userRepository = userRepository;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
        if (!user.isEnabled()) {
            throw new AccountNotEnabledException(username);
        }
        return new UserMediator(user);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        return user.get();
    }

    @Override
    public User deleteUser(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return userOptional.get();
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUnverifiedUsers() {
        List<EmailVerificationToken> expiredTokens = emailVerificationRepository.findByExpiryDateBefore(new Date());
        for (EmailVerificationToken expiredToken : expiredTokens) {
            userRepository.delete(expiredToken.getUser());
        }
        emailVerificationRepository.deleteAll(expiredTokens);
    }
}