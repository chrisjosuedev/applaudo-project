package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.OrderDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.IGeneralOrderResponse;
import dev.applaudostudios.applaudofinalproject.dto.responses.OrderResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.service.IOrderService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Operation(summary = "Fetch all user orders with general information.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched all the orders from database related with an user.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IGeneralOrderResponse.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to give you information.",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> getOrders(Principal principal) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Orders found.",
                HttpStatus.OK,
                orderService.findAllOrders(username));
    }

    @Operation(summary = "Get Order with Detail By Id.",
            description = "Given ID, fetch order detail with information related with the order user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch user order with detail from database related with an user.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to give you the order information.",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "User Order not found.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(Principal principal,
                                               @PathVariable("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Order found.",
                HttpStatus.OK,
                orderService.findOrderById(id, username));
    }

    @Operation(summary = "Place an Order defining the payment method and address.",
            description = "You can specify the address and payment to which you want to relate the order, changing the default ones.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Order with Payment and Order Information Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect or Cart is Empty.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you place an order.",
                    content = @Content)
    })
    @PostMapping("/place-order")
    public ResponseEntity<Object> placeAnOrder(Principal principal,
                                               @Valid @RequestBody OrderDto orderDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Order placed.",
                HttpStatus.CREATED,
                orderService.createOrder(orderDto, username));
    }

    @Operation(summary = "Place an Order with one-clik.",
            description = "The order will be related to the default payment method and address the default ones.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Order with Payment and Order Information Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect or Cart is Empty.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you place an order.",
                    content = @Content)
    })
    @PostMapping("/place-order/one-click")
    public ResponseEntity<Object> placeAnOrderByOneClick(Principal principal) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Order One-Click placed.",
                HttpStatus.CREATED,
                orderService.createOneClickOrder(username));
    }

    @Operation(summary = "Update delivery status.",
            description = "Update Delivery status with Tracking Number to true as Delivered.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Order Delivery Status Updated.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Path variables incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update order.",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Order by Tracking number not found.",
                    content = @Content)
    })
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
