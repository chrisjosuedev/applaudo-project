package dev.applaudostudios.applaudofinalproject.utils.helpers;

import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.repository.UserRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InfoCredential {
    @Autowired
    private UserRepository userRepository;

    public User findUserInSession(String username) {
        Optional<User> currentLoggedUser = userRepository.findByUsername(username);
        if (currentLoggedUser.isEmpty()) {
            throw new MyBusinessException("Current user doesn't exists or session is invalid.", HttpStatus.FORBIDDEN);
        }
        return currentLoggedUser.get();
    }

}
