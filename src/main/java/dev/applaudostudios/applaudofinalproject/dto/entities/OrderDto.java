package dev.applaudostudios.applaudofinalproject.dto.entities;

import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.Payment;
import dev.applaudostudios.applaudofinalproject.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class OrderDto {
    @NotNull(message = "Payment is required.")
    private Payment payment;

    @NotNull(message = "Address is required.")
    private Address address;

    private User user;
}
