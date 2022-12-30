package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.service.ICheckoutService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/checkout")
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


    @PutMapping("/my-cart")
    public ResponseEntity<Object> updateCheckout(Principal principal,
                                                 @RequestParam("productid") Long productId,
                                                 @RequestParam("quantity") Integer quantity){
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        CheckoutResponseDto checkoutUpdated = checkoutService.updateCheckout(productId, quantity, username);

        return ResponseHandler.responseBuilder("Product in cart updated successfully.",
                HttpStatus.OK,
                (checkoutUpdated == null) ? Collections.emptyList() : checkoutUpdated);
    }

}
