package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
class CheckoutRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CheckoutRepository checkoutRepository;
    @Autowired
    private ProductRepository productRepository;

    private Validator validator;

    private User user1, user2;
    private Product product1, product2;
    private CartItemSession checkout1, checkout2;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user1 = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .status(true)
                .build();

        user2 = User.builder()
                .sid("5679")
                .username("temp")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .status(true)
                .build();

        userRepository.saveAll(List.of(user1, user2));

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

        productRepository.saveAll(List.of(product1, product2));

        checkout1 = CartItemSession.builder()
                .product(product1)
                .user(user1)
                .quantity(2)
                .build();

        checkout2 = CartItemSession.builder()
                .product(product2)
                .user(user1)
                .quantity(1)
                .build();

        checkoutRepository.saveAll(List.of(checkout1, checkout2));
    }

    @AfterEach
    void tearDown() {
        checkoutRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("My JPA Queries")
    class MyJpaTest {
        @Test
        @DisplayName("FindCart by User ID and returns List checkout")
        void givenCartItemSession_WhenFindAllWithExistingCart_thenCartList() {
            List<CartItemSession> myCart = checkoutRepository.findAllByUserSid(user1.getSid());
            assertThat(myCart.size()).isGreaterThan(0);
        }

        @Test
        @DisplayName("FindCart by User ID with no cart and returns empty List")
        void givenCartItemSession_WhenFindAllWithNoExistingUserCart_thenCartListIsEmpty() {
            List<CartItemSession> myCart = checkoutRepository.findAllByUserSid(anyString());
            assertThat(myCart.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("FindCart with Complete Information")
        void givenCartItemSession_WhenFindAllWithInformation_thenCartListInformation() {
            List<ICheckoutResponseDto> myCart = checkoutRepository.getCartInformation(user1.getSid());
            assertThat(myCart.size()).isEqualTo(2);
            assertThat(myCart.get(0).getSubTotal()).isEqualTo(64.64);
        }

        @Test
        @DisplayName("FindCart with Complete Information but Non Existing User")
        void givenCartItemSession_WhenFindAllWithInformationAndNonExistingUser_thenCartListIsEmpty() {
            List<ICheckoutResponseDto> myCart = checkoutRepository.getCartInformation(anyString());
            assertThat(myCart.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("FindSubTotal from CartItemSession Details")
        void givenCartItemSession_WhenGetSubTotal_thenReturnsSubTotal() {
            Double st = checkoutRepository.getCartTotal(user1.getSid());
            assertThat(st).isEqualTo(77.86);
        }

        @Test
        @DisplayName("FindSubTotal from CartItemSession Details and Non Existing User")
        void givenCartItemSession_WhenGetSubTotalAndNonExistingUser_thenReturnsNull() {
            Double st = checkoutRepository.getCartTotal(anyString());
            assertThat(st).isNull();
        }

        @Test
        @DisplayName("FindProductInCart By User Sid and Product Id")
        void givenCartItemSession_WhenFindByUserSidAndProductId_thenProductIsPresent() {
            Optional<CartItemSession> myCart = checkoutRepository.findByProductIdAndUserSid(
                    product1.getId(),
                    user1.getSid()
            );

            assertTrue(myCart.isPresent());
            assertThat(myCart.get().getProduct())
                    .usingRecursiveComparison()
                    .isEqualTo(product1);
        }

        @Test
        @DisplayName("FindProductInCart By User Sid and Product Id When User doesnt that product in Cart")
        void givenCartItemSession_WhenFindByUserSidAndProductIdWithNonExisting_thenProductIsNotPresent() {
            Optional<CartItemSession> myCart = checkoutRepository.findByProductIdAndUserSid(
                    product1.getId(),
                    user2.getSid()
            );
            assertTrue(myCart.isEmpty());
        }

        @Test
        @DisplayName("Delete all items from Cart given an user")
        void givenCartItemSession_WhenDeleteAllCartITems_thenSuccessfull() {
            checkoutRepository.deleteAllByUserSid(user1.getSid());
            List<CartItemSession> myCart = checkoutRepository.findAllByUserSid(user1.getSid());
            assertTrue(myCart.isEmpty());
        }
    }

    @Nested
    @DisplayName("JPA Implementations | Exceptions")
    class JpaImplementations {
        @Test
        @DisplayName("Try create an Checkout with Invalid Quantity and Product")
        void givenCartItemSession_WhenCreateACheckoutWithInvalidQuantityAndProduct_thenSetConstraintsList() {
            CheckoutDto checkoutTest = CheckoutDto.builder()
                    .quantity(-2)
                    .user(user1)
                    .build();

            Set<ConstraintViolation<CheckoutDto>> violations = validator.validate(checkoutTest);
            assertThat(violations.size()).isEqualTo(2);
        }
    }
}