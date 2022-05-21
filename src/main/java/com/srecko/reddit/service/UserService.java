package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;

import java.util.List;

public interface UserService {

    boolean isUsernameAlreadyInUse(String username);
    boolean isEmailAlreadyInUse(String email);
    List<User> getUsers();
    User getUser(String username);
    User deleteUser(User user);
}
