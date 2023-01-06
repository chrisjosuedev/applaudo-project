package dev.applaudostudios.applaudofinalproject.dto.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class UserUpdateDto {
    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Invalid telephone number.")
    @Min(value = 8, message = "Invalid telephone number")
    private String telephone;
}
