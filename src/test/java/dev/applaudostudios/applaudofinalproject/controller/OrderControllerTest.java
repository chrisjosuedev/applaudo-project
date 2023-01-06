package dev.applaudostudios.applaudofinalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.UserResDto;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import dev.applaudostudios.applaudofinalproject.models.*;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import dev.applaudostudios.applaudofinalproject.service.imp.OrderService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(OrderController.class)
@Import(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private Principal principal;
    @MockBean
    private JwtDecoder jwtDecoder;
    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Product product2;
    private CartItemSession checkout1;
    private PaymentType paymentType;
    private Payment payment;
    private Address address;
    private Order order;
    private OrderDto orderDto, orderDtoInvalid;
    private OrderResponseDto orderResponseDto;

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

        orderDtoInvalid = OrderDto.builder().build();

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
    @DisplayName("Order Controller Test")
    class OrderControllerApiTest {
        @Test
        @DisplayName("CreateOrder then return Status Created")
        void givenOrder_whenCreateOrder_thenReturnCreated() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(orderService.createOrder(orderDto, user.getUsername()))
                    .willReturn(orderResponseDto);

            ResultActions response = mockMvc.perform(post("/orders/place-order")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Order placed.")));
        }

        @Test
        @DisplayName("CreateOrder One Click then return Status Created")
        void givenOrder_whenCreateOneClickOrder_thenReturnCreated() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(orderService.createOneClickOrder(user.getUsername()))
                    .willReturn(orderResponseDto);

            ResultActions response = mockMvc.perform(post("/orders/place-order/one-click")
                    .contentType(MediaType.APPLICATION_JSON)
            );

            response.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Order One-Click placed.")));
        }

        @Test
        @DisplayName("Update Delivery Status Order return Status Ok")
        void givenOrder_whenUpdateDeliveryStatus_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(orderService.updateOrder(true, order.getTrackNum(), user.getUsername()))
                    .willReturn(orderResponseDto);

            ResultActions response = mockMvc.perform(
                    put("/orders/tracking/1234/status/true")
                    .contentType(MediaType.APPLICATION_JSON)
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Updated order delivery status.")));
        }

        @Test
        @DisplayName("Get Order By Id")
        void givenOrder_whenFindOrderById_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(orderService.findOrderById(order.getId(), user.getUsername()))
                    .willReturn(orderResponseDto);

            ResultActions response = mockMvc.perform(
                    get("/orders/1")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Order found.")));
        }
    }

    @Nested
    @DisplayName("Order Controller Test Exception")
    class OrderControllerApiTestException {
        @Test
        @DisplayName("CreateOrder then return Status Bad Request")
        void givenOrder_whenCreateOrder_thenReturnCreated() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(orderService.createOrder(orderDtoInvalid, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(post("/orders/place-order")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
}