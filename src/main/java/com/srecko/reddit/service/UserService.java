package com.srecko.reddit.service;

import com.srecko.reddit.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User deleteUser(String username);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    User save(User user);
    void deleteUnverifiedUsers();
}
