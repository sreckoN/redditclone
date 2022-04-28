package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void saveUser(User user);
}
