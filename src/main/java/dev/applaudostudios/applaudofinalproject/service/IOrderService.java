package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;

public interface IOrderService {
    OrderResponseDto createOrder(OrderDto orderDto, String username);
}
