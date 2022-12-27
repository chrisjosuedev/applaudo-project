package dev.applaudostudios.applaudofinalproject.dto.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class UserDto {
    private String sid;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
