package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.models.Payment;
import dev.applaudostudios.applaudofinalproject.service.IPaymentService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping
    public ResponseEntity<Object> findAllPayments(
            Principal principal,
            @RequestParam(required = false, name = "from")
            Integer from,
            @RequestParam(required = false, name = "limit")
            Integer limit
    ) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();

        List<Payment> allPayments = paymentService.findAll(from, limit, username);

        return ResponseHandler.responseBuilder(
                "All payments registered.",
                HttpStatus.OK,
                PagResponseDto.<Payment>builder()
                        .count(allPayments.size())
                        .listFound(allPayments)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findPaymentById(Principal principal,
                                                  @PathVariable("id") Long id) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Payment Found.",
                HttpStatus.OK,
                paymentService.findPaymentById(id, username));

    }

    @PostMapping
    public ResponseEntity<Object> createPayment(
            Principal principal, @Valid @RequestBody PaymentDto paymentDto) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Payment created successfully.",
                HttpStatus.OK,
                paymentService.createPayment(paymentDto, username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePayment(Principal principal,
                                                @PathVariable("id") Long id,
                                                @Valid @RequestBody PaymentDto paymentDto) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Payment updated successfully.",
                HttpStatus.OK,
                paymentService.updatePayment(id, paymentDto, username));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePayment(Principal principal,
                                                @PathVariable("id") Long id) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Payment removed successfully.",
                HttpStatus.OK,
                paymentService.deletePayment(id, username));
    }
}
