package dev.applaudostudios.applaudofinalproject.dto.entities;

import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
public class CheckoutDto {
    @Column
    @Positive(message = "Price must be greater than 0")
    @NotNull(message = "Unit Price is required")
    private int quantity;

    @NotNull(message = "Product is required.")
    private Product product;

    private User user;
}
