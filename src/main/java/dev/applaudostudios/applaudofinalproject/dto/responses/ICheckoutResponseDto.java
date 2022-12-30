package dev.applaudostudios.applaudofinalproject.dto.responses;

public interface ICheckoutResponseDto {
    Long getProductId();
    String getProductName();
    double getUnitPrice();
    int getQuantity();
    double getSubTotal();
}
