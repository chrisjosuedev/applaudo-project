package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.Payment;
import dev.applaudostudios.applaudofinalproject.models.PaymentType;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.util.List;
import java.util.UUID;

@DataJpaTest
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private UserRepository userRepository;

    private Validator validator;

    private Payment payment1, payment2;
    private PaymentType paymentType;
    private User user;

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

        userRepository.save(user);

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
                .status(true)
                .type(paymentType)
                .user(user)
                .isDefault(true)
                .build();

        paymentRepository.saveAll(List.of(payment1, payment2));
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

    }

    @Nested
    @DisplayName("JPA Implementations")
    class JpaImplementations {

    }
}
