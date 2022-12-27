package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.auth.LoginReqDto;
import dev.applaudostudios.applaudofinalproject.dto.auth.LoginResDto;
import dev.applaudostudios.applaudofinalproject.dto.auth.TokenReqDto;
import dev.applaudostudios.applaudofinalproject.dto.general.ResponseDto;
import dev.applaudostudios.applaudofinalproject.service.imp.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> signIn(@Valid @RequestBody LoginReqDto loginReqDto) {
        return new ResponseEntity<>(authService.login(loginReqDto), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@Valid @RequestBody TokenReqDto token) {
        return new ResponseEntity<>(authService.logout(token), HttpStatus.OK);
    }
}
