package dev.applaudostudios.applaudofinalproject.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private Integer numberOfItems;
    private List<ICheckoutResponseDto> myCart;
    private double total;
}
