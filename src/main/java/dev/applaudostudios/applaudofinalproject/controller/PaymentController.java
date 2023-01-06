package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import dev.applaudostudios.applaudofinalproject.service.IPaymentService;
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
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/payments")
@Tag(name = "Payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Operation(summary = "Fetch all payments from database.",
            description = "Get all payments registered in database related with user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payments found successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagResponseDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you fetch payments.",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> findAllPayments(
            Principal principal,
            @RequestParam(required = false, name = "from")
            @Min(value = 0, message = "From must be positive number.") Integer from,
            @RequestParam(required = false, name = "limit")
            @Positive(message = "From must be greater than 0.") Integer limit
    ) {
        String username = jwtDecoder.userCredentials(principal);

        List<Payment> allPayments = paymentService.findAll(from, limit, username);

        return ResponseHandler.responseBuilder(
                "All payments registered.",
                HttpStatus.OK,
                PagResponseDto.<Payment>builder()
                        .count(allPayments.size())
                        .listFound(allPayments)
                        .build());
    }

    @Operation(summary = "Get a Payment By Id.",
            description = "Get Payment information given id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment found successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you fetch a product.",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Payment not found in database.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> findPaymentById(Principal principal,
                                                  @PathVariable("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Payment Found.",
                HttpStatus.OK,
                paymentService.findPaymentById(id, username));

    }

    @Operation(summary = "Add a new Payment to database.",
            description = "Add an payment related with user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Payment created successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect, product already exists.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you add a payment.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createPayment(
            Principal principal, @Valid @RequestBody PaymentDto paymentDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Payment created successfully.",
                HttpStatus.CREATED,
                paymentService.createPayment(paymentDto, username));
    }

    @Operation(summary = "Update a Payment.",
            description = "Update Payment information given id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment updated successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update a payment.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePayment(Principal principal,
                                                @PathVariable("id") Long id,
                                                @Valid @RequestBody PaymentDto paymentDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Payment updated successfully.",
                HttpStatus.OK,
                paymentService.updatePayment(id, paymentDto, username));

    }

    @Operation(summary = "Delete a Payment.",
            description = "Delete Payment information given id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment removed successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNull.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Payment Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Payment without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you remove a payment.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePayment(Principal principal,
                                                @PathVariable("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Payment removed successfully.",
                HttpStatus.OK,
                paymentService.deletePayment(id, username));
    }
}
