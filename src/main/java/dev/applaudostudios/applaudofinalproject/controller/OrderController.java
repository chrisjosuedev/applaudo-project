package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.service.IOrderService;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private JwtDecoder jwtDecoder;

    @GetMapping
    public ResponseEntity<Object> getOrders(Principal principal) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Orders found.",
                HttpStatus.OK,
                orderService.findAllOrders(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(Principal principal,
                                               @PathVariable("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Order found.",
                HttpStatus.OK,
                orderService.findOrderById(id, username));
    }

    @PostMapping("/place-order")
    public ResponseEntity<Object> placeAnOrder(Principal principal,
                                               @Valid @RequestBody OrderDto orderDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Order placed.",
                HttpStatus.CREATED,
                orderService.createOrder(orderDto, username));
    }

    @PostMapping("/place-order/one-click")
    public ResponseEntity<Object> placeAnOrderByOneClick(Principal principal) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Order One-Click placed.",
                HttpStatus.CREATED,
                orderService.createOneClickOrder(username));
    }

    @PutMapping("/tracking/{tracking_num}/status/{status}")
    public ResponseEntity<Object> updateDeliveryStatus(Principal principal,
                                                       @PathVariable("tracking_num") String trackNum,
                                                       @PathVariable("status") Boolean status) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Updated order delivery status.",
                HttpStatus.OK,
                orderService.updateOrder(status, trackNum, username));
    }
}
