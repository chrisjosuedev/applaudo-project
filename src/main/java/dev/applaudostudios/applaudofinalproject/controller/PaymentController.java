package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.entity.Payment;
import dev.applaudostudios.applaudofinalproject.service.IPaymentService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.JwtDecoder;
import org.keycloak.representations.AccessToken;
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
        AccessToken accessToken = JwtDecoder.userCredentials(principal);
        String username = accessToken.getPreferredUsername();

        List<Payment> allPayments = paymentService.findAll(from, limit, username);

        return ResponseHandler.responseBuilder(
                "All payments registered.",
                HttpStatus.OK,
                PagResponseDto.<Payment>builder()
                        .count(allPayments.size())
                        .listFound(allPayments)
                        .build());
    }

    @PostMapping
    public ResponseEntity<Object> createPayment(
            Principal principal, @Valid @RequestBody PaymentDto paymentDto) {
        AccessToken accessToken = JwtDecoder.userCredentials(principal);
        String username = accessToken.getPreferredUsername();
        return ResponseHandler.responseBuilder("Payment created successfully.",
                HttpStatus.OK,
                paymentService.createPayment(paymentDto, username));
    }

}
