package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dao.UserDao;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.JwtDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(JwtDecoder.class);
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll(Integer limit, Integer from) {
        List<User> allUsers = userDao.findAll();

        if (limit == null || from == null) {
            return allUsers;
        }

        if (limit < 0 || from < 0) {
            throw new MyBusinessException("Limit and From must be greater than zero.", HttpStatus.BAD_REQUEST);
        }

        Page<User> usersPaginated = userDao.findAll(PageRequest.of(from, limit));
        allUsers = usersPaginated.getContent();
        return allUsers;
    }

    private Optional<User> findById(String sid) {
        return userDao.findBySid(sid);
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> userFound = userDao.findByUsername(username);
        if (userFound.isEmpty()) {
            throw new MyBusinessException("User not found with given username.", HttpStatus.NOT_FOUND);
        }

        return userDtoResponse(userFound.get());
    }

    @Override
    public void createUser(String token) {
        UserDto userLogged = JwtDecoder.getUserInfo(token);
        Optional<User> userFound = userDao.findBySid(userLogged.getSid());

        if (userFound.isEmpty()) {
            User newUser = userFromToken(userLogged);
            userDao.save(newUser);
        }

        logger.info("User already exists, nothing added.");
    }

    @Override
    public UserDto updateUser(String sid, UserUpdateDto user) {
        Optional<User> userFound = findById(sid);

        if (userFound.isEmpty()) {
            throw new MyBusinessException("User with given id doesn't exists.", HttpStatus.NOT_FOUND);
        }

        userFound.get().setFirstName(user.getFirstName());
        userFound.get().setLastName(user.getLastName());
        userFound.get().setTelephone(user.getTelephone());

        userDao.save(userFound.get());

        return userDtoResponse(userFound.get());
    }

    @Override
    public List<User> deleteUser(String sid) {
        Optional<User> userFound = findById(sid);

        if (userFound.isEmpty()) {
            throw new MyBusinessException("User with given id doesn't exists.", HttpStatus.NOT_FOUND);
        }

        userFound.get().setStatus(false);
        userDao.save(userFound.get());

        return Collections.emptyList();
    }

    private UserDto userDtoResponse(User user) {
        return UserDto.builder()
                .sid(user.getSid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .username(user.getUsername())
                .build();
    }

    private User userFromToken(UserDto userLogged) {
        return User.builder()
                .sid(userLogged.getSid())
                .firstName(userLogged.getFirstName())
                .lastName(userLogged.getLastName())
                .email(userLogged.getEmail())
                .username(userLogged.getUsername())
                .telephone(userLogged.getTelephone())
                .status(true)
                .build();
    }
}
