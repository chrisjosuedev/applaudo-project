package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;

public interface ICheckoutService {
    CartResponseDto findMyCart(String username);
    CheckoutResponseDto addItemToCart(CheckoutDto checkoutDto, String username);
    CheckoutResponseDto updateCheckout(Long productId, Integer quantity, String username);
}
