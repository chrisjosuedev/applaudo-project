package dev.applaudostudios.applaudofinalproject.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.UserResDto;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.UserRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserHelper {
    @Autowired
    private UserRepository userRepository;

    public User findUserInSession(String username) {
        Optional<User> currentLoggedUser = userRepository.findByUsername(username);
        if (currentLoggedUser.isEmpty()) {
            throw new MyBusinessException("Current user doesn't exists or session is invalid.", HttpStatus.FORBIDDEN);
        }
        return currentLoggedUser.get();
    }

    public UserDto userDtoResponse(User user) {
        return UserDto.builder()
                .sid(user.getSid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .username(user.getUsername())
                .build();
    }

    public User userFromToken(UserDto userLogged) {
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

    public UserResDto userToOrderInfoDto(User user) {
        return UserResDto.builder()
                .sid(user.getSid())
                .fullName(user.getFirstName().concat(" ").concat(user.getLastName()))
                .username(user.getUsername())
                .build();
    }

}
