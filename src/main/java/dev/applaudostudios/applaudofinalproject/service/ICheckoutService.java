package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;

public interface ICheckoutService {
    CheckoutResponseDto addItemToCart(CheckoutDto checkoutDto, String username);
    CartResponseDto findMyCart(String username);

}
