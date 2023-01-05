package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.IGeneralOrderResponse;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.UserResDto;
import dev.applaudostudios.applaudofinalproject.helpers.db.*;
import dev.applaudostudios.applaudofinalproject.models.*;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
import dev.applaudostudios.applaudofinalproject.repository.OrderRepository;
import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailService orderDetailService;
    @Mock
    private UserHelper userHelper;
    @Mock
    private PaymentHelper paymentHelper;
    @Mock
    private AddressHelper addressHelper;
    @Mock
    private CheckoutHelper checkoutHelper;
    @Mock
    private OrderHelper orderHelper;
    private User user;
    private Product product2;
    private CartItemSession checkout1;
    private PaymentType paymentType;
    private Payment payment;
    private Address address;
    private Order order;
    private OrderDto orderDto;
    private OrderResponseDto orderResponseDto;
    private List<IGeneralOrderResponse> respDto = Collections.emptyList();

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

        product2 = Product.builder()
                .productName("Pulsera")
                .description("Con rallas")
                .unitPrice(13.22)
                .stock(12)
                .status(true)
                .build();

        checkout1 = CartItemSession.builder()
                .product(product2)
                .user(user)
                .quantity(2)
                .build();

        paymentType = PaymentType.builder()
                .type("CREDIT CARD")
                .build();

        payment = Payment.builder()
                .id(1L)
                .ccNumber("4263982640269299")
                .ccExpirationDate("4/23")
                .provider("VISA")
                .status(true)
                .type(paymentType)
                .user(user)
                .isDefault(true)
                .build();

        address = Address.builder()
                .id(1L)
                .street("Barrio Torondon")
                .city("Comayagua")
                .state("Comayagua")
                .country("Honduras")
                .zipCode("12101")
                .user(user)
                .status(true)
                .isDefault(true)
                .build();

        order = Order.builder()
                .id(1L)
                .user(user)
                .payment(payment)
                .trackNum("1234")
                .address(address)
                .status(false)
                .build();

        orderDto = OrderDto.builder()
                .payment(payment)
                .address(address)
                .build();

        orderResponseDto = OrderResponseDto.builder()
                .total(32.32)
                .userInfo(UserResDto.builder()
                        .username(user.getUsername())
                        .sid(user.getSid())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .build())
                .shippingAddress(address)
                .payment(payment)
                .date(new Date())
                .status(!order.isStatus() ? "Paid and shipped.":"Delivered.")
                .trackingNum(order.getTrackNum())
                .checkoutDetails(Collections.emptyList())
                .build();
    }

    @Nested
    @DisplayName("Checkout Service Tests")
    class OrderServiceImpTest {

        @Test
        @DisplayName("Place an Order with Payment and Address")
        void givenNewOrder_whenCreateANewOrder_thenPlaceOrder() {

            given(orderRepository.save(order)).willReturn(order);
            doNothing().when(orderDetailService).createOrders(order, List.of(checkout1));

            given(checkoutHelper.findAll(user.getSid())).willReturn(List.of(checkout1));

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), user)).willReturn(address);
            given(paymentHelper.findUserPayment(payment.getId(), user)).willReturn(payment);

            given(orderHelper.orderFromDto(payment, address, user)).willReturn(order);

            given(orderHelper.orderResponseDto(order)).willReturn(orderResponseDto);


            OrderResponseDto newOrder = orderService.createOrder(orderDto, user.getUsername());

            assertThat(newOrder).isNotNull();
        }

        @Test
        @DisplayName("Place an Order with One Click")
        void givenNewOrder_whenCreateANewOrderOneClick_thenPlaceOrder() {

            given(orderRepository.save(order)).willReturn(order);
            doNothing().when(orderDetailService).createOrders(order, List.of(checkout1));

            given(checkoutHelper.findAll(user.getSid())).willReturn(List.of(checkout1));

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            given(paymentHelper.findPayment(user)).willReturn(payment);
            given(addressHelper.findAddress(user)).willReturn(address);

            given(orderHelper.orderFromDto(payment, address, user)).willReturn(order);

            given(orderHelper.orderResponseDto(order)).willReturn(orderResponseDto);


            OrderResponseDto newOrder = orderService.createOneClickOrder(user.getUsername());

            assertThat(newOrder).isNotNull();
        }

        @Test
        @DisplayName("Update Delivery Status of Order")
        void givenOrder_whenUpdateDeliveryStatus_thenUpdateStatus() {
            given(orderRepository.save(order)).willReturn(order);
            given(orderRepository.findByTrackNumAndUserSid(order.getTrackNum(), user.getSid()))
                    .willReturn(Optional.of(order));

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            orderResponseDto.setStatus("Delivered.");

            given(orderHelper.orderResponseDto(order)).willReturn(orderResponseDto);

            OrderResponseDto statusOrder = orderService.updateOrder(
                    true,
                    order.getTrackNum(),
                    user.getUsername());

            assertThat(statusOrder).isNotNull();
            assertThat(statusOrder.getStatus()).isEqualTo("Delivered.");
        }

        @Test
        @DisplayName("Find Order By Id")
        void givenOrderId_whenFindOrderById_thenReturnOrder() {
            given(orderHelper.orderResponseDto(order)).willReturn(orderResponseDto);

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(orderHelper.findById(order.getId(), user.getSid())).willReturn(order);

            OrderResponseDto orderFound = orderService.findOrderById(order.getId(), user.getUsername());

            assertThat(orderFound).isNotNull();
        }

        @Test
        @DisplayName("FindAll Orders")
        void givenOrders_whenFindAllOrders_thenReturnList() {
            given(orderHelper.orderResponseDto(order)).willReturn(orderResponseDto);

            given(orderRepository.findAllOrdersByUserSid(user.getSid())).willReturn(respDto);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            List<IGeneralOrderResponse> allOrders = orderService.findAllOrders(user.getUsername());

            assertThat(allOrders).isNotNull();
        }

    }

    @Nested
    @DisplayName("Checkout Service Tests | Exceptions")
    class OrderServiceImpTestException {
        @Test
        @DisplayName("Place an Order with Invalid Payment")
        void givenNewOrder_whenCreateANewOrderWithInvalidPayment_thenThrowsAnException() {

            given(orderRepository.save(order)).willReturn(order);
            doNothing().when(orderDetailService).createOrders(order, List.of(checkout1));

            given(checkoutHelper.findAll(user.getSid())).willReturn(List.of(checkout1));

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), user)).willReturn(address);
            given(paymentHelper.findUserPayment(payment.getId(), user)).willThrow(
                    MyBusinessException.class
            );

            given(orderHelper.orderFromDto(payment, address, user)).willReturn(order);

            assertThrows(MyBusinessException.class, () -> {
                orderService.createOrder(orderDto, user.getUsername());
            });

            verify(orderHelper, never()).orderResponseDto(order);
        }

        @Test
        @DisplayName("Place an Order with Invalid Address")
        void givenNewOrder_whenCreateANewOrderWithInvalidAddress_thenThrowsAnException() {

            given(orderRepository.save(order)).willReturn(order);
            doNothing().when(orderDetailService).createOrders(order, List.of(checkout1));

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), user)).willThrow(
                    MyBusinessException.class
            );
            given(paymentHelper.findUserPayment(payment.getId(), user)).willReturn(payment);

            assertThrows(MyBusinessException.class, () -> {
                orderService.createOrder(orderDto, user.getUsername());
            });

            verify(orderHelper, never()).orderResponseDto(order);
        }

        @Test
        @DisplayName("Update Delivery Status with Invalid Tracking Number")
        void givenOrder_whenUpdateDeliveryStatus_thenThrowsAnException() {
            given(orderRepository.save(order)).willReturn(order);
            given(orderRepository.findByTrackNumAndUserSid(anyString(), anyString()))
                    .willThrow(MyBusinessException.class);

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            assertThrows(MyBusinessException.class, () -> {
                orderService.updateOrder(
                        true,
                        order.getTrackNum(),
                        user.getUsername());
            });

            verify(orderHelper, never()).orderResponseDto(order);
        }
    }

}
