package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User saveUser(User user);
    boolean isUsernameAlreadyInUse(String username);
    boolean isEmailAlreadyInUse(String email);
    List<User> getUsers();
    User getUser(String username);
}
