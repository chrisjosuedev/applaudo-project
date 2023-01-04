package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.service.IAddressService;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
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
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @GetMapping
    public ResponseEntity<Object> findAllProducts(
            Principal principal,
            @RequestParam(required = false, name = "from")
            @Min(value = 0, message = "From must be positive number.") Integer from,
            @RequestParam(required = false, name = "limit")
            @Positive(message = "From must be greater than 0.") Integer limit
    ) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();

        List<Address> allAddresses = addressService.findAll(from, limit, username);

        return ResponseHandler.responseBuilder(
                "All addresses registered.",
                HttpStatus.OK,
                PagResponseDto.<Address>builder()
                        .count(allAddresses.size())
                        .listFound(allAddresses)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAddressById(Principal principal,
                                                @PathVariable("id") Long id) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Address found.",
                HttpStatus.OK,
                addressService.findAddressById(id, username));
    }

    @PostMapping
    public ResponseEntity<Object> getAllAddresses(
            Principal principal, @Valid @RequestBody AddressDto addressDto) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Address created successfully.",
                HttpStatus.OK,
                addressService.createAddress(addressDto, username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAddress(Principal principal,
                                                @PathVariable("id") Long id,
                                                @Valid @RequestBody AddressDto addressDto) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Address updated successfully.",
                HttpStatus.OK,
                addressService.updateAddress(id, addressDto, username));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAddress(Principal principal,
                                                @PathVariable("id") Long id) {
        String username = JwtDecoder.userCredentials(principal).getPreferredUsername();
        return ResponseHandler.responseBuilder("Address removed successfully.",
                HttpStatus.OK,
                addressService.deleteAddress(id, username));
    }
}
