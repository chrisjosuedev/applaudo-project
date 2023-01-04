package dev.applaudostudios.applaudofinalproject.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.MyMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CheckoutHelper {
    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private ProductRepository productRepository;

    // Logic Methods
    public CartItemSession findUserCart(Long productId, String sid) {
        Optional<CartItemSession> cartProductFound = checkoutRepository
                .findByProductIdAndUserSid(productId, sid);

        if (cartProductFound.isEmpty()) {
            throw new MyBusinessException("User doesn't have a product in the cart with given id", HttpStatus.BAD_REQUEST);
        }

        return  cartProductFound.get();
    }

    public Product findProduct(Long id, double quantity) {
        Product productFound = findProduct(id);

        if (quantity > productFound.getStock()) {
            throw new MyBusinessException("Quantity entered exceeds the available in stock.", HttpStatus.BAD_REQUEST);
        }

        return productFound;
    }

    public Product findProduct(Long id) {
        Optional<Product> productFound = productRepository.findByIdAndStatusIsTrue(id);

        if (productFound.isEmpty()) {
            throw new MyBusinessException("Product with given id doesn't exists.", HttpStatus.NOT_FOUND);
        }

        return productFound.get();
    }

    public List<CartItemSession> findAll(String sid) {
        List<CartItemSession> myCart = checkoutRepository.findAllByUserSid(sid);
        if (myCart.isEmpty()) {
            throw new MyBusinessException("Cart is empty, you need at least 1 product to process the order.",
                    HttpStatus.BAD_REQUEST);
        }
        return myCart;
    }

    // Build DTO's

    public CartResponseDto myCartInformation(List<ICheckoutResponseDto> myCart, Double total) {
        return CartResponseDto.builder()
                .numberOfItems(myCart.size())
                .myCart(myCart)
                .total((total == null) ? 0.00 : MyMath.round(total, 2))
                .build();
    }

    public CartItemSession cartFromDto(CheckoutDto checkoutDto) {
        return CartItemSession.builder()
                .product(checkoutDto.getProduct())
                .user(checkoutDto.getUser())
                .quantity(checkoutDto.getQuantity())
                .build();
    }

    public CheckoutResponseDto cartResponse(CartItemSession cartItemSession) {
        double subTotal = (cartItemSession.getQuantity() * cartItemSession.getProduct().getUnitPrice());
        return CheckoutResponseDto.builder()
                .productName(cartItemSession.getProduct().getProductName())
                .unitPrice(cartItemSession.getProduct().getUnitPrice())
                .quantity(cartItemSession.getQuantity())
                .subTotal(subTotal)
                .build();
    }
}
