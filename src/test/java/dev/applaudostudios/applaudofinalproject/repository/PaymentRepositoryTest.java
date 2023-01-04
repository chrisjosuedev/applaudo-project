package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import dev.applaudostudios.applaudofinalproject.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private UserRepository userRepository;

    private Validator validator;

    private Payment payment1, payment2, payment3;
    private PaymentType paymentType;
    private User user, user2;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        paymentType = PaymentType.builder()
                .type("CREDIT CARD")
                .build();

        paymentTypeRepository.save(paymentType);

        user = User.builder()
                .sid("1234")
                .email("cmartinez@gmail.com")
                .username("chrisjosuel")
                .firstName("Chris")
                .lastName("Martinez")
                .telephone("+50498398923")
                .status(true)
                .build();

        user2 = User.builder()
                .sid("5678")
                .email("temporal@gmail.com")
                .username("temporal")
                .firstName("Temp")
                .lastName("Martinez")
                .telephone("+50498498923")
                .status(true)
                .build();

        userRepository.saveAll(List.of(user, user2));

        payment1 = Payment.builder()
                .ccNumber("4263982640269299")
                .ccExpirationDate("4/23")
                .provider("VISA")
                .status(true)
                .type(paymentType)
                .user(user)
                .isDefault(true)
                .build();

        payment2 = Payment.builder()
                .ccNumber("2223000048410010")
                .ccExpirationDate("2/25")
                .provider("MASTER CARD")
                .status(false)
                .type(paymentType)
                .user(user)
                .isDefault(false)
                .build();

        payment3 = Payment.builder()
                .ccNumber("2113000048410010")
                .ccExpirationDate("2/24")
                .provider("MASTER CARD")
                .status(false)
                .type(paymentType)
                .user(user2)
                .isDefault(false)
                .build();

        paymentRepository.saveAll(List.of(payment1, payment2, payment3));
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
        paymentTypeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("My JPA Queries")
    class MyJpaTest {
        @Test
        @DisplayName("FindCardByCcNumber with an existing card")
        void givenCcNumber_WhenFindByCcNumberWithExistingCard_thenCardIsPresent() {
            Optional<Payment> paymentFound = paymentRepository.findPaymentByCcNumber(payment1.getCcNumber());
            Assertions.assertTrue(paymentFound.isPresent());
            assertThat(paymentFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(payment1);
        }

        @Test
        @DisplayName("FindCardByCcNumber with an non existing card")
        void givenCcNumber_WhenFindByCcNumberWithNonExistingCard_thenCardIsNotPresent() {
            Optional<Payment> paymentFound = paymentRepository.findPaymentByCcNumber(anyString());
            assertThat(paymentFound).isEmpty();
        }

        @Test
        @DisplayName("FindPayment and verify user's default payment")
        void givenUserSid_WhenFindPaymentWithUserDefault_thenDefaultIsPresent() {
            Optional<Payment> paymentFound = paymentRepository.findPayment(payment1.getUser().getSid());
            Assertions.assertTrue(paymentFound.isPresent());
            assertThat(paymentFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(payment1);
        }

        @Test
        @DisplayName("FindPayment and verify user doesn't have a default payment")
        void givenUserSid_WhenFindPaymentWithUserNoDefault_thenDefaultIsNotPresent() {
            Optional<Payment> paymentFound = paymentRepository.findPayment(payment3.getUser().getSid());
            assertThat(paymentFound).isEmpty();
        }

        @Test
        @DisplayName("FindByIdAndStatusIsTrueAndUserSid with a Active Payment Method")
        void givenUserSidAndCardId_WhenFindWithAnActiveCard_thenIsPresent() {
            Optional<Payment> paymentFound = paymentRepository.findByIdAndStatusIsTrueAndUserSid(
                    payment1.getId(),
                    payment1.getUser().getSid());

            Assertions.assertTrue(paymentFound.isPresent());
            assertThat(paymentFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(payment1);
        }

        @Test
        @DisplayName("FindByIdAndStatusIsTrueAndUserSid with a Non Active Payment Method")
        void givenUserSidAndCardId_WhenFindWithAnActiveCard_thenIsNotPresent() {
            Optional<Payment> paymentFound = paymentRepository.findByIdAndStatusIsTrueAndUserSid(
                    payment2.getId(),
                    payment2.getUser().getSid());

            assertThat(paymentFound).isEmpty();
        }

        @Test
        @DisplayName("FindAllByUserSid With Active Payment Methods")
        void givenUserSid_WhenFindActiveCards_thenPaymentListSizeWithStatusIsTrue() {
            List<Payment> myPayments = paymentRepository.findAllByUserSidAndStatusIsTrue(payment1.getUser().getSid());
            assertThat(myPayments.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("FindAllByUserSid With Active Payment Methods with paginable")
        void givenUserSid_WhenFindActiveCards_thenPaymentPaginationListSizeWithStatusIsTrue() {
            List<Payment> myPayments = paymentRepository.findAllByUserSidAndStatusIsTrue(
                    payment1.getUser().getSid(),
                    PageRequest.of(0, 1));
            assertThat(myPayments.size()).isEqualTo(1);
            assertThat(myPayments.get(0).getProvider()).isEqualTo("VISA");
        }

        @Test
        @DisplayName("FindAllByUserSid with an non existing User")
        void givenInvalidUserSid_WhenFindActiveCards_thenResturnEmptyList() {
            List<Payment> myPayments = paymentRepository.findAllByUserSidAndStatusIsTrue(anyString());
            assertThat(myPayments.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("JPA Implementations | Exceptions")
    class JpaImplementations {
        @Test
        @DisplayName("Save a Payment with incorrect CC Number Length get Constraint Exceptions")
        void givenPaymentData_WhenSavePaymentWithInvalidCcNumberLength_thenConstraintSetList() {
            Payment paymentTest = Payment.builder()
                    .ccNumber("fsdfsdfsdf")
                    .ccExpirationDate("2/24")
                    .provider("MASTER CARD")
                    .status(true)
                    .type(paymentType)
                    .user(user2)
                    .isDefault(false)
                    .build();

            Set<ConstraintViolation<Payment>> violations = validator.validate(paymentTest);
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("Save a Payment with incorrect Expiration Date get Constraint Exceptions")
        void givenPaymentData_WhenSavePaymentWithInvalidDate_thenConstraintSetList() {
            Payment paymentTest = Payment.builder()
                    .ccNumber("4363482640269342")
                    .ccExpirationDate("fsd")
                    .provider("MASTER CARD")
                    .status(true)
                    .type(paymentType)
                    .user(user2)
                    .isDefault(false)
                    .build();

            Set<ConstraintViolation<Payment>> violations = validator.validate(paymentTest);
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("Save a Payment with no Payment Type get Constraint Exceptions")
        void givenPaymentData_WhenSavePaymentWithNullType_thenConstraintSetList() {
            PaymentDto paymentTest = PaymentDto.builder()
                    .ccNumber("4363482640269342")
                    .ccExpirationDate("3/25")
                    .provider("MASTER CARD")
                    .isDefault(false)
                    .build();

            Set<ConstraintViolation<PaymentDto>> violations = validator.validate(paymentTest);
            assertThat(violations.size()).isEqualTo(1);
        }
    }
}
