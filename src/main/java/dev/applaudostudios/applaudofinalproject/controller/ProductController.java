package dev.applaudostudios.applaudofinalproject.controller;

import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.PagResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.responses.ResponseHandler;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RolesAllowed("ADMIN")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<Object> findAllProducts(
            @RequestParam(required = false, name = "limit")
            Integer limit,
            @RequestParam(required = false, name = "from")
            Integer from
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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") Long id) {
        return ResponseHandler.responseBuilder(
                "User Found.",
                HttpStatus.OK,
                productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseHandler.responseBuilder(
                "Product created successfully.",
                HttpStatus.CREATED,
                productService.createProduct(productDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id,
                                             @Valid @RequestBody ProductDto productDto) {
        return ResponseHandler.responseBuilder(
                "Product updated successfully.",
                HttpStatus.OK,
                productService.updateProduct(id, productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long id) {
        return ResponseHandler.responseBuilder("Product removed successfully.",
                HttpStatus.OK,
                productService.deleteProduct(id));
    }
}
