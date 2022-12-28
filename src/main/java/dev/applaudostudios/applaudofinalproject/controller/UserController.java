package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<Object> findAllUsers(
            @RequestParam(required = false, name = "limit")
            Integer limit,
            @RequestParam(required = false, name = "from")
            Integer from
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

    @PutMapping("/{sid}")
    public ResponseEntity<Object> updateUser(@PathVariable("sid") String sid,
                                             @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseHandler.responseBuilder("User updated successfully.",
                HttpStatus.OK,
                userService.updateUser(sid, userUpdateDto));
    }

    @DeleteMapping("/{sid}")
    public ResponseEntity<Object> updateUser(@PathVariable("sid") String sid) {
        return ResponseHandler.responseBuilder("User removed successfully.",
                HttpStatus.OK,
                userService.deleteUser(sid));
    }

}
