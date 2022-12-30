package dev.applaudostudios.applaudofinalproject.dto.entities;

import dev.applaudostudios.applaudofinalproject.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
public class AddressDto {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "Invalid City name length")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "Invalid State name length")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Invalid Country name length")
    private String country;

    @NotBlank(message = "Zip Code is required")
    @Size(min = 5, max = 5, message = "Zip code must have 5 characters")
    private String zipCode;

    @NotNull(message = "Default Status value is required.")
    private boolean isDefault;

    private User user;
}
