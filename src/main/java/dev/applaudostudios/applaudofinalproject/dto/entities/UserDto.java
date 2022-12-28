package dev.applaudostudios.applaudofinalproject.dto.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private String sid;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private String username;
}
