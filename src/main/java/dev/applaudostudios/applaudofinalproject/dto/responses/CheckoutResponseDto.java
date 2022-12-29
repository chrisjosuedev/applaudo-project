package dev.applaudostudios.applaudofinalproject.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponseDto {
    private String productName;
    private double unitPrice;
    private int quantity;
    private double subTotal;
}
