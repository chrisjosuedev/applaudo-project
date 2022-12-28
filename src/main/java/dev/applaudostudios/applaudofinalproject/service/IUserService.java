package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    UserDto findByUsername(String username);

    void createUser(String token);

    UserDto updateUser(String sid, UserUpdateDto user);

    List<User> deleteUser(String sid);
}
