package dev.applaudostudios.applaudofinalproject.dto.entities;

import dev.applaudostudios.applaudofinalproject.entity.PaymentType;
import dev.applaudostudios.applaudofinalproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
public class PaymentDto {
    @Length(min = 16, max = 19, message = "Account number must be between 16-19 characters.")
    private String ccNumber;

    @NotBlank(message = "Provider is required.")
    private String provider;

    @NotBlank(message = "Expiration date is required.")
    @Pattern(regexp = "(?:0?[1-9]|1[0-2])/[0-9]{2}", message = "Invalid expiration date")
    private String ccExpirationDate;

    @NotNull(message = "Default Status value is required.")
    private boolean isDefault;

    @NotNull(message = "Payment Type cannot be null")
    private PaymentType type;

    private User user;
}
