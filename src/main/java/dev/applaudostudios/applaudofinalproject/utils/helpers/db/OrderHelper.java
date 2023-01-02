package dev.applaudostudios.applaudofinalproject.utils.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Order;
import dev.applaudostudios.applaudofinalproject.models.OrderDetail;
import dev.applaudostudios.applaudofinalproject.repository.OrderDetailRepository;
import dev.applaudostudios.applaudofinalproject.repository.OrderRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.patterns.MyMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderHelper {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserHelper userHelper;

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
        List<ICheckoutResponseDto> myOrders =
                orderDetailRepository.getOrderDetailInformation(order.getId());
        Double totalOrder = orderDetailRepository.getTotal(order.getId());

        return OrderResponseDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .checkoutDetails(myOrders)
                .payment(order.getPayment())
                .userInfo(userHelper.userToOrderInfoDto(order.getUser()))
                .shippingAddress(order.getAddress())
                .trackingNum(order.getTrackNum())
                .total(MyMath.round(totalOrder, 2))
                .status(true)
                .build();
    }

    public Order findById(Long id, String sid) {
        Optional<Order> orderFound = orderRepository.findByIdAndUserSid(id, sid);
        if (orderFound.isEmpty()) {
            throw new MyBusinessException("User doesn't have and Order with given id.",
                    HttpStatus.BAD_REQUEST);
        }
        return orderFound.get();
    }
}
