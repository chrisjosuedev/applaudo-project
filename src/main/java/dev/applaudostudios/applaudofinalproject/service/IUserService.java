package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.models.User;

import java.util.List;

public interface IUserService {
    List<User> findAll(Integer limit, Integer from);

    UserDto findByUsername(String username);

    void createUser(String token);

    UserDto updateUser(String username, UserUpdateDto user);

    Object deleteUser(String username);
}
