package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.auth.LoginReqDto;
import dev.applaudostudios.applaudofinalproject.dto.auth.LoginResDto;
import dev.applaudostudios.applaudofinalproject.dto.auth.TokenReqDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseDto;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.service.imp.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Allow request token to Keycloack Auth Server")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Request an Access Token.",
            description = "Login in API, create user is doesn't exists and returns access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Login OK.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> signIn(@Valid @RequestBody LoginReqDto loginReqDto) {
        return new ResponseEntity<>(authService.login(loginReqDto), HttpStatus.OK);
    }

    @Operation(summary = "Close current session.",
            description = "Logout from AuthServer, remove token access.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Logout OK.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content)
    })
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@Valid @RequestBody TokenReqDto token) {
        return new ResponseEntity<>(authService.logout(token), HttpStatus.OK);
    }
}
