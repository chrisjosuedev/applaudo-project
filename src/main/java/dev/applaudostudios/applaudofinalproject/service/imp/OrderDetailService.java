package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Order;
import dev.applaudostudios.applaudofinalproject.models.OrderDetail;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
import dev.applaudostudios.applaudofinalproject.repository.OrderDetailRepository;
import dev.applaudostudios.applaudofinalproject.service.IOrderDetailsService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Transactional
@Service
public class OrderDetailService implements IOrderDetailsService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Override
    public void createOrders(Order order, List<CartItemSession> myCart) {
        List<OrderDetail> myDetail = new LinkedList<>();

        myCart.forEach((cartItem) -> {
            myDetail.add(
                    OrderDetail.builder()
                            .order(order)
                            .product(cartItem.getProduct())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getProduct().getUnitPrice())
                            .build()
            );
        });

        orderDetailRepository.saveAll(myDetail);
        checkoutRepository.deleteAllByUserSid(order.getUser().getSid());
    }
}
