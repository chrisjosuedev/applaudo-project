package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;

public interface ICheckoutService {
    CartResponseDto findMyCart(String username);
    CheckoutResponseDto addItemToCart(CheckoutDto checkoutDto, String username);
    Object updateCheckout(Long productId, Integer quantity, String username);
    Object deleteCheckout(Long productId, String username);
    Object deleteAllCheckout(String username);
}
