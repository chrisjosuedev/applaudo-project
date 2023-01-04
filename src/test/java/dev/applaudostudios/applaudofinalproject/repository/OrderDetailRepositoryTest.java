package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.IGeneralOrderResponse;
import dev.applaudostudios.applaudofinalproject.models.*;
import dev.applaudostudios.applaudofinalproject.models.Order;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderDetailRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CheckoutRepository checkoutRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    private User user1;
    private Product product1, product2;
    private CartItemSession checkout1, checkout2;
    private Address address;
    private Payment payment;
    private PaymentType paymentType;
    private Order order;
    private OrderDetail orderDetail1, orderDetail2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .status(true)
                .build();

        userRepository.save(user1);

        address = Address.builder()
                .street("Barrio Torondon")
                .city("Comayagua")
                .state("Comayagua")
                .country("Honduras")
                .zipCode("12101")
                .user(user1)
                .status(true)
                .isDefault(true)
                .build();

        addressRepository.save(address);

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

        paymentType = PaymentType.builder()
                .type("CREDIT CARD")
                .build();

        paymentTypeRepository.save(paymentType);

        payment = Payment.builder()
                .ccNumber("4263982640269299")
                .ccExpirationDate("4/23")
                .provider("VISA")
                .status(true)
                .type(paymentType)
                .user(user1)
                .isDefault(true)
                .build();

        paymentRepository.save(payment);

        order = Order.builder()
                .address(address)
                .payment(payment)
                .user(user1)
                .trackNum("1234")
                .status(true)
                .build();

        orderRepository.save(order);

        orderDetail1 = OrderDetail.builder()
                .quantity(checkout2.getQuantity())
                .price(checkout2.getProduct().getUnitPrice())
                .order(order)
                .product(checkout2.getProduct())
                .build();

        orderDetail2 = OrderDetail.builder()
                .quantity(checkout1.getQuantity())
                .price(checkout1.getProduct().getUnitPrice())
                .order(order)
                .product(checkout1.getProduct())
                .build();

        orderDetailRepository.saveAll(List.of(orderDetail1, orderDetail2));
    }

    @AfterEach
    void tearDown() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        addressRepository.deleteAll();
        paymentRepository.deleteAll();
        paymentTypeRepository.deleteAll();
        checkoutRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("My Jpa Queries")
    class MyJpaTest {
        @Test
        @DisplayName("FindOrdersDetail by Id with existing Order")
        void givenOrderId_WhenFindOrderById_thenOrderDetailList() {
            List<ICheckoutResponseDto> myOrders = orderDetailRepository.getOrderDetailInformation(order.getId());
            assertThat(myOrders.size()).isGreaterThan(0);
            assertThat(myOrders.get(0).getProductName()).isEqualTo(orderDetail1.getProduct().getProductName());
        }

        @Test
        @DisplayName("FindOrdersDetail by Id with non existing Order")
        void givenOrderId_WhenFindOrderByIdNonExisting_thenOrderDetailListIsEmpty() {
            List<ICheckoutResponseDto> myOrders = orderDetailRepository.getOrderDetailInformation(213L);
            assertThat(myOrders.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Find Subtotal by Id")
        void givenOrderId_WhenFindOrderById_thenReturnsSubTotal() {
            Double st = orderDetailRepository.getTotal(order.getId());
            assertThat(st).isEqualTo(77.86);
        }

        @Test
        @DisplayName("Find Subtotal by Id with Non Existing Id")
        void givenOrderId_WhenFindOrderByIdNoExisting_thenReturnsNull() {
            Double st = orderDetailRepository.getTotal(423L);
            assertThat(st).isNull();
        }
    }
}