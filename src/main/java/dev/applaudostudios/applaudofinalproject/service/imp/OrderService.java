package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.models.*;
import dev.applaudostudios.applaudofinalproject.repository.OrderRepository;
import dev.applaudostudios.applaudofinalproject.service.IOrderDetailsService;
import dev.applaudostudios.applaudofinalproject.service.IOrderService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentHelper paymentHelper;

    @Autowired
    private AddressHelper addressHelper;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private OrderHelper orderHelper;

    @Autowired
    private CheckoutHelper checkoutHelper;

    @Autowired
    private IOrderDetailsService orderDetailsService;

    @Override
    public OrderResponseDto createOrder(OrderDto orderDto, String username) {
        User currentLoggedUser = userHelper.findUserInSession(username);
        List<CartItemSession> myCart = checkoutHelper.findAll(currentLoggedUser.getSid());

        Payment paymentFound = paymentHelper.findUserPayment(orderDto.getPayment().getId(), currentLoggedUser);
        Address addressFound = addressHelper.findUserAddress(orderDto.getAddress().getId(), currentLoggedUser);
        orderDto.setPayment(paymentFound);
        orderDto.setAddress(addressFound);
        orderDto.setUser(currentLoggedUser);

        // Generate new Order
        Order newOrder = orderHelper.orderFromDto(orderDto);
        orderRepository.save(newOrder);

        orderDetailsService.createOrders(newOrder, myCart);

        return orderHelper.orderResponseDto(newOrder);
    }

    @Override
    public OrderResponseDto findOrderById(Long id, String username) {
        User currentLoggedUser = userHelper.findUserInSession(username);
        Order orderFound = orderHelper.findById(id, currentLoggedUser.getSid());
        return orderHelper.orderResponseDto(orderFound);
    }

    @Override
    public List<Order> findAllOrders(String username) {
        User currentLoggedUser = userHelper.findUserInSession(username);
        return orderRepository.findAllByUserSid(currentLoggedUser.getSid());
    }
}
