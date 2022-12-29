package dev.applaudostudios.applaudofinalproject.dto.responses;

public interface ICheckoutResponseDto {
    String getProductName();
    double getUnitPrice();
    int getQuantity();
    double getSubTotal();
}
