package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.service.IAddressService;
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
@RequestMapping("/addresses")
@Tag(name = "Addresses")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Operation(summary = "Fetch all addresses from database.",
            description = "Get all addresses registered in database related with user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Address found successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagResponseDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you fetch addresses.",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> findAllProducts(
            Principal principal,
            @RequestParam(required = false, name = "from")
            @Min(value = 0, message = "From must be positive number.") Integer from,
            @RequestParam(required = false, name = "limit")
            @Positive(message = "From must be greater than 0.") Integer limit
    ) {
        String username = jwtDecoder.userCredentials(principal);

        List<Address> allAddresses = addressService.findAll(from, limit, username);

        return ResponseHandler.responseBuilder(
                "All addresses registered.",
                HttpStatus.OK,
                PagResponseDto.<Address>builder()
                        .count(allAddresses.size())
                        .listFound(allAddresses)
                        .build());
    }

    @Operation(summary = "Get a Address By Id.",
            description = "Get Address information given id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Address found successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Address.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you fetch a address.",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Address not found in database.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAddressById(Principal principal,
                                                @PathVariable("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Address found.",
                HttpStatus.OK,
                addressService.findAddressById(id, username));
    }

    @Operation(summary = "Add a new Address to database.",
            description = "Add an address related with user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Address created successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Address.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you add a address.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createAddress(
            Principal principal, @Valid @RequestBody AddressDto addressDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Address created successfully.",
                HttpStatus.CREATED,
                addressService.createAddress(addressDto, username));
    }

    @Operation(summary = "Update a Address.",
            description = "Update address information given id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Address updated successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Address.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update an address.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAddress(Principal principal,
                                                @PathVariable("id") Long id,
                                                @Valid @RequestBody AddressDto addressDto) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Address updated successfully.",
                HttpStatus.OK,
                addressService.updateAddress(id, addressDto, username));

    }

    @Operation(summary = "Delete a Address.",
            description = "Delete address information given id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Address removed successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNull.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Address Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Address without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you remove an address.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAddress(Principal principal,
                                                @PathVariable("id") Long id) {
        String username = jwtDecoder.userCredentials(principal);
        return ResponseHandler.responseBuilder("Address removed successfully.",
                HttpStatus.OK,
                addressService.deleteAddress(id, username));
    }
}
