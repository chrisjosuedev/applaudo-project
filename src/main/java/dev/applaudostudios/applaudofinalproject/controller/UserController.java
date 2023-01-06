package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
@Tag(name = "Users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Operation(summary = "Fetch all the users registered in database, " +
            "only admin users allowed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Fetched all the users from database",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PagResponseDto.class))}),
            @ApiResponse(responseCode = "401",
            description = "User without authentication",
            content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to give you a file.",
                    content = @Content)
    })
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

    @Operation(summary = "Get an user by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to give you a file.",
                    content = @Content)
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<Object> getUserByUsername(@PathVariable("username") String username) {
        return ResponseHandler.responseBuilder("User Found.",
                HttpStatus.OK, userService.findByUsername(username));
    }

    @Operation(summary = "Update User Information",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User updated successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to give you a file.",
                    content = @Content)
    })
    @PutMapping
    public ResponseEntity<Object> updateUser(Principal principal,
                                             @Valid @RequestBody UserUpdateDto userUpdateDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("User updated successfully.",
                HttpStatus.OK,
                userService.updateUser(username, userUpdateDto));
    }
}
