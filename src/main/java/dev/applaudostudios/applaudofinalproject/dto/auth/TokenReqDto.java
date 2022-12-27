package dev.applaudostudios.applaudofinalproject.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenReqDto {
    @NotBlank(message = "Refresh Token is required")
    private String token;
}
