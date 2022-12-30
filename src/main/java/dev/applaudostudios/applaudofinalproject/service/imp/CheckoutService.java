package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.utils.helpers.ObjectNull;
import dev.applaudostudios.applaudofinalproject.entity.CartItemSession;
import dev.applaudostudios.applaudofinalproject.entity.Product;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import dev.applaudostudios.applaudofinalproject.service.ICheckoutService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.InfoCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CheckoutService implements ICheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InfoCredential infoCredential;

    @Autowired
    private ObjectNull objectNull;

    @Override
    public CheckoutResponseDto addItemToCart(CheckoutDto checkoutDto, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        Product product = findProduct(checkoutDto.getProduct().getId(), checkoutDto.getQuantity());

        checkoutDto.setUser(currentLoggedUser);
        checkoutDto.setProduct(product);

        // Validate if already exists a product with given ID in cart
        Optional<CartItemSession> cartProductFound = checkoutRepository
                .findByProductIdAndUserSid(checkoutDto.getProduct().getId(), checkoutDto.getUser().getSid());

        if (cartProductFound.isPresent()) {
            throw new MyBusinessException("Product has already been added to cart.", HttpStatus.BAD_REQUEST);
        }

        CartItemSession newCartItem = cartFromDto(checkoutDto);
        checkoutRepository.save(newCartItem);

        // Update Stock
        product.setStock(product.getStock() - newCartItem.getQuantity());
        productRepository.save(product);

        return cartResponse(newCartItem);
    }

    @Override
    public Object updateCheckout(Long productId, Integer quantity, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        Optional<CartItemSession> cartProductFound = checkoutRepository
                .findByProductIdAndUserSid(productId, currentLoggedUser.getSid());

        if (cartProductFound.isEmpty()) {
            throw new MyBusinessException("User doesn't have a product in the cart with given id", HttpStatus.BAD_REQUEST);
        }

        Product product = findProduct(productId, quantity);

        return objectNull.getObjectNull();
    }

    @Override
    public CartResponseDto findMyCart(String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        List<ICheckoutResponseDto> myCart = checkoutRepository.getCartInformation(currentLoggedUser.getSid());
        Double totalCart = checkoutRepository.getCartTotal(currentLoggedUser.getSid());
        return myCartInformation(myCart, totalCart);
    }

    private Product findProduct(Long id, int quantity) {
        Optional<Product> productFound = productRepository.findByIdAndStatusIsTrue(id);

        if (productFound.isEmpty()) {
            throw new MyBusinessException("Product with given id doesn't exists.", HttpStatus.NOT_FOUND);
        }
        if (quantity > productFound.get().getStock()) {
            throw new MyBusinessException("Quantity entered exceeds the available in stock.", HttpStatus.BAD_REQUEST);
        }
        return productFound.get();
    }

    private CartResponseDto myCartInformation(List<ICheckoutResponseDto> myCart, Double total) {
        return CartResponseDto.builder()
                .numberOfItems(myCart.size())
                .myCart(myCart)
                .total((total == null) ? 0.00 : total)
                .build();
    }

    private CartItemSession cartFromDto(CheckoutDto checkoutDto) {
        return CartItemSession.builder()
                .product(checkoutDto.getProduct())
                .user(checkoutDto.getUser())
                .quantity(checkoutDto.getQuantity())
                .build();
    }

    private CheckoutResponseDto cartResponse(CartItemSession cartItemSession) {
        double subTotal = (cartItemSession.getQuantity() * cartItemSession.getProduct().getUnitPrice());
        return CheckoutResponseDto.builder()
                .productName(cartItemSession.getProduct().getProductName())
                .unitPrice(cartItemSession.getProduct().getUnitPrice())
                .quantity(cartItemSession.getQuantity())
                .subTotal(subTotal)
                .build();
    }
}
