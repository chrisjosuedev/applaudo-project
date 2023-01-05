package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.helpers.db.CheckoutHelper;
import dev.applaudostudios.applaudofinalproject.helpers.db.UserHelper;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CheckoutServiceTest {
    @InjectMocks
    private CheckoutService checkoutService;
    @Mock
    private CheckoutRepository checkoutRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserHelper userHelper;
    @Mock
    private CheckoutHelper checkoutHelper;
    @Mock
    private ObjectNull objectNull;
    private User user;
    private Product product1, product2;
    private CartItemSession checkout1;
    private CheckoutDto checkoutDto, checkoutDto2;
    private CheckoutResponseDto checkoutResponseDto;
    private List<ICheckoutResponseDto> respDto = Collections.emptyList();
    private CartResponseDto cartResponseDto;
    private Double subTotal;

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

        checkoutDto = CheckoutDto.builder()
                .product(product1)
                .quantity(1)
                .user(user)
                .build();

        checkout1 = CartItemSession.builder()
                .product(checkoutDto.getProduct())
                .user(user)
                .quantity(2)
                .build();

        subTotal = (checkout1.getQuantity() * checkout1.getProduct().getUnitPrice());
        checkoutResponseDto = CheckoutResponseDto.builder()
                .productName(checkout1.getProduct().getProductName())
                .unitPrice(checkout1.getProduct().getUnitPrice())
                .quantity(checkout1.getQuantity())
                .subTotal(subTotal)
                .build();

        cartResponseDto = CartResponseDto.builder()
                .myCart(Collections.emptyList())
                .total(subTotal)
                .numberOfItems(1)
                .build();

    }

    @Nested
    @DisplayName("Checkout Service Tests")
    class CheckoutServiceImpTest {
        @Test
        @DisplayName("FindMyCart")
        void givenCart_whenFindMyCart_thenReturnCart() {
            given(checkoutRepository.getCartTotal(user.getSid())).willReturn(subTotal);
            given(checkoutRepository.getCartInformation(user.getSid())).willReturn(respDto);
            given(checkoutHelper.myCartInformation(respDto, subTotal)).willReturn(cartResponseDto);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            CartResponseDto myCart = checkoutService.findMyCart(user.getUsername());

            assertThat(myCart).isNotNull();
        }

        @Test
        @DisplayName("Add a new Item to Checkout")
        void givenCart_whenAddANewItem_thenReturnCart() {
            given(checkoutRepository.save(checkout1)).willReturn(checkout1);
            given(productRepository.save(product1)).willReturn(product1);
            given(checkoutHelper.cartResponse(checkout1)).willReturn(checkoutResponseDto);
            given(checkoutHelper.cartFromDto(checkoutDto)).willReturn(checkout1);
            given(checkoutHelper.findProduct(product1.getId(), 1)).willReturn(product1);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            CheckoutResponseDto newCart = checkoutService.addItemToCart(
                    checkoutDto, user.getUsername());

            assertThat(newCart).isNotNull();
        }

        @Test
        @DisplayName("Update an Item to Checkout")
        void givenCart_whenUpdateItem_thenReturnCart() {
            given(checkoutRepository.save(checkout1)).willReturn(checkout1);

            given(checkoutHelper.findUserCart(product1.getId(), user.getSid()))
                    .willReturn(checkout1);
            given(checkoutHelper.findProduct(product1.getId(), 2)).willReturn(product1);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            given(checkoutHelper.cartFromDto(checkoutDto)).willReturn(checkout1);
            given(checkoutHelper.cartResponse(checkout1)).willReturn(checkoutResponseDto);


            Object updatedCart = checkoutService.updateCheckout(
                    product1.getId(),
                    2,
                    user.getUsername()
            );

            assertThat(updatedCart).isNotNull();
            assertThat(((CheckoutResponseDto) updatedCart).getQuantity()).isEqualTo(2);
        }

        @Test
        @DisplayName("Update an Item to Checkout | Returns Object Null")
        void givenCart_whenUpdateItemWithZero_thenReturnObjectNull() {
            doNothing().when(checkoutRepository).delete(checkout1);

            given(checkoutHelper.findUserCart(product1.getId(), user.getSid()))
                    .willReturn(checkout1);
            given(checkoutHelper.findProduct(product1.getId(), 0)).willReturn(product1);

            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            given(checkoutHelper.cartFromDto(checkoutDto)).willReturn(checkout1);
            given(checkoutHelper.cartResponse(checkout1)).willReturn(checkoutResponseDto);


            Object removedCart = checkoutService.updateCheckout(
                    product1.getId(),
                    0,
                    user.getUsername()
            );

            assertEquals(Collections.emptyList(), removedCart);
        }

        @Test
        @DisplayName("Delete Item in Checkout")
        void givenCart_whenRemoveItem_thenObjectNull() {
            doNothing().when(checkoutRepository).delete(checkout1);

            given(productRepository.save(product1)).willReturn(product1);

            given(checkoutHelper.findProduct(product1.getId())).willReturn(product1);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(checkoutHelper.findUserCart(product1.getId(), user.getSid())).willReturn(checkout1);

            Object removedCart = checkoutService.deleteCheckout(product1.getId(), user.getUsername());

            assertEquals(Collections.emptyList(), removedCart);
        }

        @Test
        @DisplayName("Delete All Checkout")
        void givenCart_whenRemoveAllCheckout_thenObjectNull() {
            doNothing().when(checkoutRepository).deleteAllByUserSid(user.getSid());

            given(checkoutRepository.findAllByUserSid(user.getSid()))
                    .willReturn(List.of(checkout1));

            given(productRepository.save(product1)).willReturn(product1);

            given(checkoutHelper.findProduct(product1.getId())).willReturn(product1);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(checkoutHelper.findUserCart(product1.getId(), user.getSid())).willReturn(checkout1);

            Object removedCart = checkoutService.deleteAllCheckout(user.getUsername());

            assertEquals(Collections.emptyList(), removedCart);
        }
    }

    @Nested
    @DisplayName("Checkout Service Tests Exception")
    class CheckoutServiceImpTestException {
        @Test
        @DisplayName("Item to Checkout With Existing Product")
        void givenCart_whenAddANewItem_thenReturnCart() {
            given(checkoutRepository.save(checkout1)).willReturn(checkout1);
            given(productRepository.save(product1)).willReturn(product1);

            given(checkoutRepository.findByProductIdAndUserSid(product1.getId(), user.getSid()))
                    .willThrow(MyBusinessException.class);
            given(checkoutHelper.findProduct(product1.getId(), 1)).willReturn(product1);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);

            assertThrows(MyBusinessException.class, () -> {
                checkoutService.addItemToCart(checkoutDto, user.getUsername());
            });

            verify(checkoutHelper, never()).cartResponse(checkout1);
        }

        @Test
        @DisplayName("Delete Item in Checkout No Existing Product")
        void givenCart_whenRemoveItemWithNoExistingProduct_thenThrowsAnException() {
            doNothing().when(checkoutRepository).delete(checkout1);

            given(productRepository.save(product1)).willReturn(product1);

            given(checkoutHelper.findProduct(product1.getId())).willReturn(product1);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(checkoutHelper.findUserCart(product1.getId(), user.getSid()))
                    .willThrow(MyBusinessException.class);

            assertThrows(MyBusinessException.class, () -> {
                checkoutService.deleteCheckout(product1.getId(), user.getUsername());
            });

            verify(objectNull, never()).getObjectNull();
        }
    }

}