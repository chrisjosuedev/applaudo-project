package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.CheckoutDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CartResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.service.ICheckoutService;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Checkout")
public class CheckoutController {

    @Autowired
    private ICheckoutService checkoutService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Operation(summary = "Add a new Item to Checkout.",
            description = "Add an item to cart with at least one product.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Checkout created with product.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CheckoutResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect, product doesn't exists or quantity stock exceeded.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update checkout.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createCheckout(Principal principal,
                                                 @Valid @RequestBody CheckoutDto checkoutDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Item added to cart successfully.",
                HttpStatus.CREATED,
                checkoutService.addItemToCart(checkoutDto, username));
    }

    @Operation(summary = "Get items in Cart.",
            description = "Items in my Cart with Information related with quantity and total.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch Cart Information.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update checkout.",
                    content = @Content)
    })
    @GetMapping("/my-cart")
    public ResponseEntity<Object> getMyCart(Principal principal) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Current cart by user: " + username,
                HttpStatus.OK,
                checkoutService.findMyCart(username));
    }

    @Operation(summary = "Update Quantity for a product.",
            description = "Update Quaitnty related with a product given product id via params.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Checkout updated successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CheckoutResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect, product doesn't exists or quantity stock exceeded.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update checkout.",
                    content = @Content)
    })
    @PutMapping("/my-cart/product/{product_id}/quantity/{quantity}")
    public ResponseEntity<Object> updateCheckout(Principal principal,
                                                 @PathVariable("product_id")
                                                 Long id,
                                                 @PathVariable("quantity")
                                                 @Min(value = 0, message = "Quantity must be greater than 0.")
                                                 Integer quantity){
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Cart updated successfully.",
                HttpStatus.OK,
                checkoutService.updateCheckout(id, quantity, username));
    }

    @Operation(summary = "Delete product/checkout from cart.",
            description = "Delete product in related checkout.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Checkout removed successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CheckoutResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect, product doesn't exists.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you delete checkout.",
                    content = @Content)
    })
    @DeleteMapping("/my-cart/product")
    public ResponseEntity<Object> deleteCheckout(Principal principal,
                                                 @RequestParam("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Product removed successfully.",
                HttpStatus.OK,
                checkoutService.deleteCheckout(id, username));
    }

    @Operation(summary = "Delete all Cart.",
            description = "Delete all checkout related with the user cart.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cart removed successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CheckoutResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you removed checkout.",
                    content = @Content)
    })
    @DeleteMapping("/my-cart")
    public ResponseEntity<Object> deleteMyCart(Principal principal) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Cart was removed successfully.",
                HttpStatus.OK,
                checkoutService.deleteAllCheckout(username));
    }

}
