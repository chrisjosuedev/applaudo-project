package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.repository.UserRepository;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.helpers.db.UserHelper;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(JwtDecoder.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserHelper userHelper;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public List<User> findAll(Integer limit, Integer from) {
        List<User> allUsers = userRepository.findAllByStatusIsTrue();

        if (limit == null || from == null) {
            return allUsers;
        }

        return userRepository.findAllByStatusIsTrue(PageRequest.of(from, limit));
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> userFound = userRepository.findByUsername(username);
        if (userFound.isEmpty()) {
            throw new MyBusinessException("User not found with given username.", HttpStatus.NOT_FOUND);
        }
        return userHelper.userDtoResponse(userFound.get());
    }

    @Override
    public void createUser(String token) {
        UserDto userLogged = jwtDecoder.getUserInfo(token);
        Optional<User> userFound = userRepository.findBySid(userLogged.getSid());
        User newUser = userHelper.userFromToken(userLogged);
        if (userFound.isPresent() && !userFound.get().isStatus()) {
            newUser.setStatus(true);
        }
        userRepository.save(newUser);
        logger.info("User already exists, nothing added.");
    }

    @Override
    public UserDto updateUser(String username, UserUpdateDto user) {
        User currentUserLogged = userHelper.findUserInSession(username);

        currentUserLogged.setFirstName(user.getFirstName());
        currentUserLogged.setLastName(user.getLastName());
        currentUserLogged.setTelephone(user.getTelephone());

        userRepository.save(currentUserLogged);

        return userHelper.userDtoResponse(currentUserLogged);
    }
}
