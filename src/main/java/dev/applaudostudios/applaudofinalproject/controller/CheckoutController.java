package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.service.ICheckoutService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequestMapping("/checkout")
@Validated
public class CheckoutController {

    @Autowired
    private ICheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<Object> createCheckout(Principal principal,
                                                 @Valid @RequestBody CheckoutDto checkoutDto) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Item added to cart successfully",
                HttpStatus.CREATED,
                checkoutService.addItemToCart(checkoutDto, username));
    }

    @GetMapping("/my-cart")
    public ResponseEntity<Object> getMyCart(Principal principal) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Current cart by user: " + username,
                HttpStatus.OK,
                checkoutService.findMyCart(username));
    }

    @PutMapping("/my-cart/product/{product_id}/quantity/{quantity}")
    public ResponseEntity<Object> updateCheckout(Principal principal,
                                                 @PathVariable("product_id")
                                                 Long id,
                                                 @PathVariable("quantity")
                                                 @Min(value = 0, message = "Quantity must be greater than 0.")
                                                 Integer quantity){
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Cart updated successfully.",
                HttpStatus.OK,
                checkoutService.updateCheckout(id, quantity, username));
    }

    @DeleteMapping("/my-cart/product")
    public ResponseEntity<Object> deleteCheckout(Principal principal,
                                                 @RequestParam("id") Long id) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Product removed successfully.",
                HttpStatus.OK,
                checkoutService.deleteCheckout(id, username));
    }

    @DeleteMapping("/my-cart")
    public ResponseEntity<Object> deleteMyCart(Principal principal) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Cart was removed successfully.",
                HttpStatus.OK,
                checkoutService.deleteAllCheckout(username));
    }

}
