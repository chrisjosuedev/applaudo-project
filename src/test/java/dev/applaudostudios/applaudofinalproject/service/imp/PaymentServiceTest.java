package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.helpers.db.PaymentHelper;
import dev.applaudostudios.applaudofinalproject.helpers.db.UserHelper;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import dev.applaudostudios.applaudofinalproject.repository.PaymentRepository;
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
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PaymentServiceTest {
    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentHelper paymentHelper;
    @Mock
    private UserHelper userHelper;
    @Mock
    private ObjectNull objectNull;
    @Mock
    private PaymentRepository paymentRepository;

    private PaymentType paymentType;
    private Payment payment;
    private PaymentDto paymentDto;
    private User user;

    @BeforeEach
    void setUp() {
        paymentType = PaymentType.builder()
                .type("CREDIT CARD")
                .build();

        user = User.builder()
                .sid("1234")
                .email("cmartinez@gmail.com")
                .username("chrisjosuel")
                .firstName("Chris")
                .lastName("Martinez")
                .telephone("+50498398923")
                .status(true)
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
                .ccExpirationDate("4/25")
                .ccNumber(paymentDto.getCcNumber())
                .provider(paymentDto.getProvider())
                .isDefault(paymentDto.isDefault())
                .user(paymentDto.getUser())
                .status(true)
                .type(paymentDto.getType())
                .build();
    }

    @Nested
    @DisplayName("Payment Service Tests")
    class PaymentServiceImpTest {
        @Test
        @DisplayName("FindAllAddress without Pagination")
        void givenUser_WhenFindAllProductsWithNoPagination_thenReturnList() {
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentRepository.findAllByUserSidAndStatusIsTrue(
                    user.getSid()
            )).willReturn(List.of(payment));

            List<Payment> allPayments = paymentService.findAll(null, null, user.getUsername());

            assertThat(allPayments.size()).isGreaterThan(0);
            assertThat(allPayments.get(0).getProvider()).isEqualTo("VISA");
        }

        @Test
        @DisplayName("FindAllAddress with Pagination")
        void givenUser_WhenFindAllProductsWithPagination_thenReturnPaginationList() {
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentRepository.findAllByUserSidAndStatusIsTrue(
                    user.getSid(),
                    PageRequest.of(1, 1)
            )).willReturn(List.of(payment));

            List<Payment> allPayments = paymentService.findAll(1, 1, user.getUsername());

            assertThat(allPayments.size()).isGreaterThan(0);
            assertThat(allPayments.get(0).getProvider()).isEqualTo("VISA");
        }
        @Test
        @DisplayName("Find a Payment By Id")
        void givenAPaymentId_whenFindPaymentById_thenReturnPaymentFound() {
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentHelper.findUserPayment(payment.getId(), user)).willReturn(payment);

            Payment paymentFound = paymentService.findPaymentById(payment.getId(), user.getUsername());

            assertThat(payment).isNotNull();
            assertThat(paymentFound.getProvider()).isEqualTo("VISA");
        }
        @Test
        @DisplayName("Create a Payment with Existing User and No Existing Card")
        void givenPayment_whenCreateANewPaymentWithValidData_thenReturnPaymentNotNull() {
            given(paymentRepository.save(payment)).willReturn(payment);

            given(paymentHelper.findCcPayment(payment.getCcNumber())).willReturn(false);
            given(paymentRepository.findPayment(user.getSid())).willReturn(Optional.of(payment));
            given(paymentHelper.findProvider(payment.getCcNumber())).willReturn(payment.getProvider());
            given(paymentHelper.findPaymentType(paymentType.getId())).willReturn(paymentType);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentHelper.paymentFromDto(paymentDto)).willReturn(payment);

            Payment newPayment = paymentService.createPayment(paymentDto, user.getUsername());

            assertThat(newPayment).isNotNull();
        }

        @Test
        @DisplayName("Update a Payment")
        void givenPayment_whenUpdateAPaymentWithValidData_thenReturnPaymentNotNull() {
            given(paymentRepository.save(payment)).willReturn(payment);

            given(paymentHelper.findCcPayment(payment.getCcNumber())).willReturn(false);
            given(paymentRepository.findPayment(user.getSid())).willReturn(Optional.of(payment));
            given(paymentHelper.findProvider(payment.getCcNumber())).willReturn(payment.getProvider());
            given(paymentHelper.findPaymentType(paymentType.getId())).willReturn(paymentType);
            given(paymentHelper.findUserPayment(payment.getId(), user)).willReturn(payment);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentHelper.paymentFromDto(paymentDto)).willReturn(payment);

            payment.setCcExpirationDate(paymentDto.getCcExpirationDate());

            Payment updatedPayment = paymentService.updatePayment(payment.getId(), paymentDto, user.getUsername());

            assertThat(updatedPayment).isNotNull();
            assertThat(updatedPayment.getCcExpirationDate()).isEqualTo(paymentDto.getCcExpirationDate());
        }

        @Test
        @DisplayName("Delete a Payment")
        void givenPayment_whenDeleteAPaymentWithValidData_thenReturnObjectNull() {
            given(paymentRepository.save(payment)).willReturn(payment);
            given(paymentHelper.findUserPayment(payment.getId(), user)).willReturn(payment);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            Object removedPayment = paymentService.deletePayment(payment.getId(), user.getUsername());

            assertEquals(Collections.emptyList(), removedPayment);
        }

    }

    @Nested
    @DisplayName("Payment Service Tests | Throw an Exception")
    class PaymentServiceImpTestException {
        @Test
        @DisplayName("Create Payment with Existing User and Existing Card")
        void givenAnPayment_whenCreateANewPaymentWithValidData_thenThrowsAnException() {
            given(paymentRepository.save(payment)).willReturn(payment);

            given(paymentHelper.findCcPayment(payment.getCcNumber())).willReturn(true);
            given(paymentRepository.findPayment(user.getSid())).willReturn(Optional.of(payment));
            given(paymentHelper.findPaymentType(paymentType.getId())).willReturn(paymentType);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentHelper.paymentFromDto(paymentDto)).willReturn(payment);

            assertThrows(MyBusinessException.class, () -> {
                paymentService.createPayment(paymentDto, user.getUsername());
            });
        }

        @Test
        @DisplayName("Update Payment with Existing User and Existing Card")
        void givenAnPayment_whenUpdatePaymentWithValidData_thenThrowsAnException() {
            given(paymentRepository.save(payment)).willReturn(payment);

            given(paymentHelper.findCcPayment(payment.getCcNumber())).willReturn(true);
            given(paymentRepository.findPayment(user.getSid())).willReturn(Optional.of(payment));
            given(paymentHelper.findPaymentType(paymentType.getId())).willReturn(paymentType);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(paymentHelper.paymentFromDto(paymentDto)).willReturn(payment);

            assertThrows(MyBusinessException.class, () -> {
                paymentService.updatePayment(payment.getId(), paymentDto, user.getUsername());
            });
        }

        @Test
        @DisplayName("Delete a Payment with Invalid Id")
        void givenPayment_whenDeleteAPaymentWithInValidData_thenThrowAnException() {
            given(paymentRepository.save(payment)).willReturn(payment);
            given(paymentHelper.findUserPayment(payment.getId(), user)).willThrow(MyBusinessException.class);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            assertThrows(MyBusinessException.class, () -> {
                paymentService.deletePayment(payment.getId(), user.getUsername());
            });
        }
    }
}