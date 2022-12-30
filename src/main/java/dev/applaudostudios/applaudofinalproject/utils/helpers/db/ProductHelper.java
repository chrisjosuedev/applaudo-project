package dev.applaudostudios.applaudofinalproject.utils.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductHelper {
    @Autowired
    private ProductRepository productRepository;

    public boolean findByName(String productName){
        Optional<Product> productFound = productRepository.findByProductNameContainingIgnoreCase(productName);
        return productFound.isPresent();
    }

    public Product productFromDto(ProductDto productDto) {
        return Product.builder()
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .stock(productDto.getStock())
                .unitPrice(productDto.getUnitPrice())
                .status(productDto.isStatus())
                .build();
    }
}
