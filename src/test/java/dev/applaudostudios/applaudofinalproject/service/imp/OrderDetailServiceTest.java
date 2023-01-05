package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.models.*;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
import dev.applaudostudios.applaudofinalproject.repository.OrderDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderDetailServiceTest {
    @InjectMocks
    private OrderDetailService orderDetailService;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private CheckoutRepository checkoutRepository;
    private User user;
    private Order order;
    private Product product1, product2;
    private OrderDetail orderDetail1, orderDetail2;
    private CartItemSession checkout1, checkout2;
    List<OrderDetail> myOrderDetail = new LinkedList<>();
    List<CartItemSession> myCheckout = new LinkedList<>();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .status(true)
                .build();

        product1 = Product.builder()
                .productName("Camisa")
                .description("Hombre")
                .unitPrice(32.32)
                .stock(23)
                .status(true)
                .build();

        product2 = Product.builder()
                .productName("Pulsera")
                .description("Con rallas")
                .unitPrice(13.22)
                .stock(12)
                .status(true)
                .build();

        checkout1 = CartItemSession.builder()
                .quantity(2)
                .product(product1)
                .user(user)
                .build();

        checkout2 = CartItemSession.builder()
                .quantity(1)
                .product(product2)
                .user(user)
                .build();

        order = Order.builder()
                .user(user)
                .build();

        orderDetail1 = OrderDetail.builder()
                .order(order)
                .product(checkout1.getProduct())
                .price(checkout1.getProduct().getUnitPrice())
                .quantity(checkout1.getQuantity())
                .build();

        orderDetail2 = OrderDetail.builder()
                .order(order)
                .product(checkout2.getProduct())
                .price(checkout2.getProduct().getUnitPrice())
                .quantity(checkout2.getQuantity())
                .build();

        myCheckout.addAll(List.of(checkout1, checkout2));
        myOrderDetail.addAll(List.of(orderDetail1, orderDetail2));

    }

    @Nested
    @DisplayName("Order Detail Service Tests")
    class OrderDetailServiceImpTest {
        @Test
        @DisplayName("Order Detail from Cart")
        void givenOrderCart_whenOrder_thenOrderDetail() {
            given(orderDetailRepository.saveAll(myOrderDetail)).willReturn(myOrderDetail);
            doNothing().when(checkoutRepository).deleteAllByUserSid(user.getSid());

            orderDetailService.createOrders(order, myCheckout);

            verify(orderDetailRepository, atLeastOnce()).saveAll(myOrderDetail);
            verify(checkoutRepository, atLeastOnce()).deleteAllByUserSid(user.getSid());
        }
    }
}