package dev.applaudostudios.applaudofinalproject.dto.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class UserDto {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
