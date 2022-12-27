package dev.applaudostudios.applaudofinalproject.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginReqDto {
    @NotBlank(message = "Username/Email is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
