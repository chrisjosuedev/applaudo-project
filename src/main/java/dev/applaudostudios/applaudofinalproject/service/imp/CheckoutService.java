package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.utils.helpers.patterns.MyMath;
import dev.applaudostudios.applaudofinalproject.utils.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.CheckoutRepository;
import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import dev.applaudostudios.applaudofinalproject.service.ICheckoutService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.db.InfoCredential;
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
        CartItemSession cartProductFound = findUserCart(productId, currentLoggedUser.getSid());

        // Verify if product is available
        Product product = findProduct(productId, quantity);

        // Update stock
        int updateQuantityAtStock = cartProductFound.getQuantity() - quantity;
        product.setStock(product.getStock() + updateQuantityAtStock);
        productRepository.save(product);

        // Update Checkout
        if (quantity == 0) {
            checkoutRepository.delete(cartProductFound);
            return objectNull.getObjectNull();
        }

        cartProductFound.setQuantity(quantity);
        checkoutRepository.save(cartProductFound);

        return cartResponse(cartProductFound);
    }

    @Override
    public Object deleteCheckout(Long productId, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        CartItemSession cartProductFound = findUserCart(productId, currentLoggedUser.getSid());

        // Update Stock
        Product product = findProduct(productId);
        product.setStock(product.getStock() + cartProductFound.getQuantity());
        productRepository.save(product);

        // Delete Checkout
        checkoutRepository.delete(cartProductFound);

        return objectNull.getObjectNull();
    }

    @Override
    public Object deleteAllCheckout(String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        List<CartItemSession> myCart = checkoutRepository.findAllByUserSid(currentLoggedUser.getSid());

        myCart.forEach((cartItem) -> {
            Product product = findProduct(cartItem.getProduct().getId());
            product.setStock(product.getStock() + cartItem.getQuantity());
            productRepository.save(product);
        });

        checkoutRepository.deleteAllByUserSid(currentLoggedUser.getSid());

        return objectNull.getObjectNull();
    }

    @Override
    public CartResponseDto findMyCart(String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        List<ICheckoutResponseDto> myCart = checkoutRepository.getCartInformation(currentLoggedUser.getSid());
        Double totalCart = checkoutRepository.getCartTotal(currentLoggedUser.getSid());
        return myCartInformation(myCart, totalCart);
    }

    // Logic Methods

    private CartItemSession findUserCart(Long productId, String sid) {
        Optional<CartItemSession> cartProductFound = checkoutRepository
                .findByProductIdAndUserSid(productId, sid);

        if (cartProductFound.isEmpty()) {
            throw new MyBusinessException("User doesn't have a product in the cart with given id", HttpStatus.BAD_REQUEST);
        }

        return  cartProductFound.get();
    }

    private Product findProduct(Long id, double quantity) {
        Product productFound = findProduct(id);

        if (quantity > productFound.getStock()) {
            throw new MyBusinessException("Quantity entered exceeds the available in stock.", HttpStatus.BAD_REQUEST);
        }

        return productFound;
    }

    private Product findProduct(Long id) {
        Optional<Product> productFound = productRepository.findByIdAndStatusIsTrue(id);

        if (productFound.isEmpty()) {
            throw new MyBusinessException("Product with given id doesn't exists.", HttpStatus.NOT_FOUND);
        }

        return productFound.get();
    }

    // Build DTO's

    private CartResponseDto myCartInformation(List<ICheckoutResponseDto> myCart, Double total) {
        return CartResponseDto.builder()
                .numberOfItems(myCart.size())
                .myCart(myCart)
                .total((total == null) ? 0.00 : MyMath.round(total, 2))
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
