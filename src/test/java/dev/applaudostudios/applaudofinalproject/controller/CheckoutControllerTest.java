package dev.applaudostudios.applaudofinalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.service.imp.CheckoutService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CheckoutController.class)
@Import(CheckoutController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {
    @Mock
    private Principal principal;
    @MockBean
    private JwtDecoder jwtDecoder;
    @MockBean
    private CheckoutService checkoutService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;
    private CheckoutDto checkoutDto, checkoutDtoInvalid;
    private Product product1;
    private CheckoutResponseDto checkoutResponseDto;
    private Double subTotal;
    private CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .build();

        product1 = Product.builder()
                .id(1L)
                .productName("Camisa")
                .description("Hombre")
                .unitPrice(32.32)
                .stock(23)
                .status(true)
                .build();

        checkoutDto = CheckoutDto.builder()
                .product(product1)
                .quantity(1)
                .user(user)
                .build();

        subTotal = (checkoutDto.getQuantity() * checkoutDto.getProduct().getUnitPrice());
        checkoutResponseDto = CheckoutResponseDto.builder()
                .productName(checkoutDto.getProduct().getProductName())
                .unitPrice(checkoutDto.getProduct().getUnitPrice())
                .quantity(checkoutDto.getQuantity())
                .subTotal(subTotal)
                .build();

        cartResponseDto = CartResponseDto
                .builder()
                .total(subTotal)
                .build();
    }

    @Nested
    @DisplayName("Checkout Controller Test")
    class CheckoutControllerApiTest {
        @Test
        @DisplayName("GetCart then return Status Ok")
        void givenCheckout_whenGetCheckout_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.findMyCart(user.getUsername()))
                    .willReturn(cartResponseDto);

            ResultActions response = mockMvc.perform(get("/checkout/my-cart")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(checkoutDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk());
        }
        @Test
        @DisplayName("CreateCheckout then return Status Created")
        void givenCheckout_whenCreateCheckout_thenReturnCreated() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.addItemToCart(checkoutDto, user.getUsername()))
                    .willReturn(checkoutResponseDto);

            ResultActions response = mockMvc.perform(post("/checkout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(checkoutDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Item added to cart successfully.")));
        }

        @Test
        @DisplayName("UpdateCheckout then return Status OK")
        void givenCheckout_whenUpdateCheckout_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.updateCheckout(product1.getId(), checkoutDto.getQuantity(), user.getUsername()))
                    .willReturn(checkoutResponseDto);

            ResultActions response = mockMvc.perform(
                    put("/checkout/my-cart/product/1/quantity/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(checkoutDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Cart updated successfully.")));
        }

        @Test
        @DisplayName("DeleteCheckout then return Status OK")
        void givenCheckout_whenDeleteCheckout_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.deleteCheckout(product1.getId(), user.getUsername()))
                    .willReturn(checkoutResponseDto);

            ResultActions response = mockMvc.perform(
                    delete("/checkout/my-cart/product?id=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(checkoutDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Product removed successfully.")));
        }

        @Test
        @DisplayName("DeleteAllCheckout then return Status OK")
        void givenCheckout_whenDeleteAllCheckout_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.deleteAllCheckout(user.getUsername()))
                    .willReturn(checkoutResponseDto);

            ResultActions response = mockMvc.perform(
                    delete("/checkout/my-cart")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                            CoreMatchers.is("Cart was removed successfully.")));
        }
    }

    @Nested
    @DisplayName("Checkout Controller Test Exception")
    class CheckoutControllerApiTestException {
        @Test
        @DisplayName("CreateCheckout Invalid Data then return Status Bad Request")
        void givenCheckout_whenCreateCheckout_thenReturnBadRequest() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.addItemToCart(checkoutDtoInvalid, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(post("/checkout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(checkoutDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        @DisplayName("UpdateCheckout Invalid Data then return Status Bad Request")
        void givenCheckout_whenUpdateCheckout_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(checkoutService.updateCheckout(product1.getId(), -32, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(
                    put("/checkout/my-cart/product/1/quantity/-32")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(checkoutDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

    }

}