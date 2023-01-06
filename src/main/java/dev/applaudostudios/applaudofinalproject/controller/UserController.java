package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<Object> findAllUsers(
            @RequestParam(required = false, name = "limit")
            @Min(value = 0, message = "From must be positive number.") Integer limit,
            @RequestParam(required = false, name = "from")
            @Positive(message = "From must be greater than 0.") Integer from
    ) {
        List<User> usersList = userService.findAll(limit, from);
        return ResponseHandler.responseBuilder("Users registered.",
                HttpStatus.OK,
                PagResponseDto.<User>builder()
                        .count(usersList.size())
                        .listFound(usersList)
                        .build()
        );
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Object> getUserByUsername(@PathVariable("username") String username) {
        return ResponseHandler.responseBuilder("User Found.",
                HttpStatus.OK, userService.findByUsername(username));
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(Principal principal,
                                             @Valid @RequestBody UserUpdateDto userUpdateDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("User updated successfully.",
                HttpStatus.OK,
                userService.updateUser(username, userUpdateDto));
    }
}
