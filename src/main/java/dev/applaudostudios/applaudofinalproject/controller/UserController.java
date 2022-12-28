package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        return ResponseHandler.responseBuilder("Active users.", HttpStatus.OK, userService.findAll());
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
