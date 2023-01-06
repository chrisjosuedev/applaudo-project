package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.CheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.service.IProductService;
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

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RolesAllowed("ADMIN")
@RequestMapping("/products")
@Tag(name = "Products", description = "Admin users allowed")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Operation(summary = "Fetch all product from database.",
            description = "Get all products registered in database.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Products found successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagResponseDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you fetch products.",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> findAllProducts(
            @RequestParam(required = false, name = "limit")
            @Min(value = 0, message = "From must be positive number.") Integer limit,
            @RequestParam(required = false, name = "from")
            @Positive(message = "From must be greater than 0.") Integer from
    ) {

        List<Product> allProducts = productService.findAll(limit, from);

        return ResponseHandler.responseBuilder(
                "All products registered.",
                HttpStatus.OK,
                PagResponseDto.<Product>builder()
                        .count(allProducts.size())
                        .listFound(allProducts)
                        .build());
    }

    @Operation(summary = "Get a Product By Id.",
            description = "Get Product information given id, only admin users allowed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product found successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
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
                    description = "Product not found in database.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") Long id) {
        return ResponseHandler.responseBuilder(
                "User Found.",
                HttpStatus.OK,
                productService.findById(id));
    }

    @Operation(summary = "Add a new Product to database.",
            description = "Add an product, only admin users allowed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Product created successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect, product already exists.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you add a product.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseHandler.responseBuilder(
                "Product created successfully.",
                HttpStatus.CREATED,
                productService.createProduct(productDto)
        );
    }

    @Operation(summary = "Update a Product.",
            description = "Update Product information given id, only admin users allowed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product updated successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update a product.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id,
                                             @Valid @RequestBody ProductDto productDto) {
        return ResponseHandler.responseBuilder(
                "Product updated successfully.",
                HttpStatus.OK,
                productService.updateProduct(id, productDto));
    }

    @Operation(summary = "Delete a Product.",
            description = "Delete Product information given id, only admin users allowed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product removed successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNull.class))}),
            @ApiResponse(responseCode = "400",
                    description = "User Information is incorrect.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "User without authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Server refuses to let you update a product.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long id) {
        return ResponseHandler.responseBuilder("Product removed successfully.",
                HttpStatus.OK,
                productService.deleteProduct(id));
    }
}
