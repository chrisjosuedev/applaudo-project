package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    User findById(String sid);

    void createUser(String token);

    User updateUser(String sid, User user);

    void deleteUser(String sid);
}
