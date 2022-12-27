package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    User findById(Long id);

    void createUser(String token);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
