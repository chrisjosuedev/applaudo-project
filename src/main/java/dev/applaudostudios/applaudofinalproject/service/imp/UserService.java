package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dao.UserDao;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.JwtDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(JwtDecoder.class);
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtDecoder decoder;

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public void createUser(String token) {
        UserDto userLogged = decoder.getUserInfo(token);
        Optional<User> userFound = userDao.findBySid(userLogged.getSid());

        // Create User if it doesn't exist
        if (userFound.isEmpty()) {
            User newUser = User.builder()
                    .sid(userLogged.getSid())
                    .firstName(userLogged.getFirstName())
                    .lastName(userLogged.getLastName())
                    .email(userLogged.getEmail())
                    .username(userLogged.getUsername())
                    .telephone("")
                    .build();

            userDao.save(newUser);
        }

        logger.info("User already exists, nothing added.");
    }

    @Override
    public User updateUser(Long id, User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
