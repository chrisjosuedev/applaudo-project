package dev.applaudostudios.applaudofinalproject.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private String sid;
    private String fullName;
    private String username;
}
