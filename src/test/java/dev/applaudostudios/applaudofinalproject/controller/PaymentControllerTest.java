package dev.applaudostudios.applaudofinalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import dev.applaudostudios.applaudofinalproject.service.imp.PaymentService;
import dev.applaudostudios.applaudofinalproject.service.imp.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PaymentController.class)
@Import(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
    @Mock
    private Principal principal;
    @MockBean
    private JwtDecoder jwtDecoder;
    @MockBean
    private PaymentService paymentService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;
    private Payment payment;
    private PaymentDto paymentDto, paymentDtoInvalid;
    private PaymentType paymentType;

    @BeforeEach
    void setUp() {
        paymentType = PaymentType.builder()
                .type("CREDIT CARD")
                .build();

        user = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .build();

        paymentDto = PaymentDto
                .builder()
                .ccNumber("4263982640269299")
                .ccExpirationDate("4/23")
                .provider("VISA")
                .type(paymentType)
                .user(user)
                .isDefault(true)
                .build();

        payment = Payment
                .builder()
                .id(1L)
                .ccExpirationDate("4/25")
                .ccNumber(paymentDto.getCcNumber())
                .provider(paymentDto.getProvider())
                .isDefault(paymentDto.isDefault())
                .user(paymentDto.getUser())
                .status(true)
                .type(paymentDto.getType())
                .build();

        paymentDtoInvalid = PaymentDto
                .builder()
                .ccExpirationDate("sdfsdf")
                .isDefault(paymentDto.isDefault())
                .build();
    }

    @Nested
    @DisplayName("Payment Controller Test")
    class PaymentControllerApiTest {
        @Test
        @DisplayName("CreatePayment then return Status OK")
        void givenPayment_whenCreatePayment_thenReturnCreated() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(paymentService.createPayment(paymentDto, user.getUsername()))
                    .willReturn(payment);

            ResultActions response = mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Payment created successfully.")));
        }

        @Test
        @DisplayName("findById then return Status OK")
        void givenPayment_whenFindById_thenReturnOK() throws Exception {
            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(paymentService.findPaymentById(payment.getId(), user.getUsername()))
                    .willReturn(payment);

            ResultActions response = mockMvc.perform(get(
                    "/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payment))
            );
            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Payment Found.")));
        }

        @Test
        @DisplayName("UpdatePayment then return Status OK")
        void givenPayment_whenUpdatePayment_thenReturnOK() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(paymentService.updatePayment(payment.getId(), paymentDto, user.getUsername()))
                    .willReturn(payment);

            ResultActions response = mockMvc.perform(put("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("DeletePayment then return Status OK")
        void givenPayment_whenDeletePayment_thenReturnOK() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(paymentService.deletePayment(payment.getId(), user.getUsername()))
                    .willReturn(any());

            ResultActions response = mockMvc.perform(delete("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("Payment Controller Test Exceptions")
    class PaymentControllerApiTestException {
        @Test
        @DisplayName("CreatePayment then return Status Bad Request")
        void givenPayment_whenCreatePayment_thenReturnBadRequest() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(paymentService.createPayment(paymentDtoInvalid, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        @DisplayName("UpdatePayment then return Status Bad Request")
        void givenPayment_whenUpdatePayment_thenReturnBadRequest() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(paymentService.updatePayment(payment.getId(), paymentDtoInvalid, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(put("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
}