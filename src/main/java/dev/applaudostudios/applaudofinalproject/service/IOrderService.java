package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.models.Order;

import java.util.List;

public interface IOrderService {
    OrderResponseDto createOrder(OrderDto orderDto, String username);
    OrderResponseDto createOneClickOrder(String username);
    OrderResponseDto updateOrder(Boolean status, String trackNum, String username);
    OrderResponseDto findOrderById(Long id, String username);
    List<Order> findAllOrders(String username);
}
