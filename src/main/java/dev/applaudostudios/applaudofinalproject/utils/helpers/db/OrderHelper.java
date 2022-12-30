package dev.applaudostudios.applaudofinalproject.utils.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.models.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
public class OrderHelper {
    public Order orderFromDto(OrderDto orderDto) {
        return Order.builder()
                .trackNum(UUID.randomUUID().toString())
                .status(true)
                .user(orderDto.getUser())
                .address(orderDto.getAddress())
                .payment(orderDto.getPayment())
                .build();
    }

    public OrderResponseDto orderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .orderDetail(Collections.emptyList())
                .payment(order.getPayment())
                .address(order.getAddress())
                .trackingNum(order.getTrackNum())
                .status(true)
                .build();
    }
}
