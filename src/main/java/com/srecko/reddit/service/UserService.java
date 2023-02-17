package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    User getUser(String username);
    User deleteUser(String username);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    User save(User user);
    void deleteUnverifiedUsers();
}
