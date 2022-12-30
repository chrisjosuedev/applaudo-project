package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.service.IProductService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.patterns.ObjectNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectNull objectNull;

    @Override
    public List<Product> findAll(Integer limit, Integer from) {
        List<Product> allProducts = productRepository.findAllByStatusIsTrue();

        if (limit == null || from == null) {
            return allProducts;
        }

        if (limit < 0 || from < 0) {
            throw new MyBusinessException("Limit and From must be greater than zero.", HttpStatus.BAD_REQUEST);
        }

        allProducts = productRepository.findAllByStatusIsTrue(PageRequest.of(from, limit));
        return allProducts;
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> productFound = productRepository.findById(id);
        if (productFound.isEmpty()) {
            throw new MyBusinessException("Product not found with Given Id.", HttpStatus.NOT_FOUND);
        }
        return productFound.get();
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        if(findByName(productDto.getProductName())) {
            throw new MyBusinessException("Already exists a Product with given name", HttpStatus.BAD_REQUEST);
        }
        Product newProduct = productFromDto(productDto);
        productRepository.save(newProduct);
        return newProduct;
    }

    @Override
    public Product updateProduct(Long id, ProductDto productDto) {
        Optional<Product> productFound = productRepository.findById(id);

        if (productFound.isEmpty()) {
            throw new MyBusinessException("Product not found with Given Id.", HttpStatus.NOT_FOUND);
        }

        productFound.get().setProductName(productDto.getProductName());
        productFound.get().setDescription(productDto.getDescription());
        productFound.get().setStock(productDto.getStock());
        productFound.get().setUnitPrice(productDto.getUnitPrice());
        productFound.get().setStatus(productDto.isStatus());

        productRepository.save(productFound.get());

        return productFound.get();
    }

    @Override
    public Object deleteProduct(Long id) {
        Optional<Product> productFound = productRepository.findById(id);

        if (productFound.isEmpty()) {
            throw new MyBusinessException("Product not found with Given Id.", HttpStatus.NOT_FOUND);
        }

        productFound.get().setStatus(false);
        productRepository.save(productFound.get());

        return objectNull.getObjectNull();
    }

    private boolean findByName(String productName){
        Optional<Product> productFound = productRepository.findByProductNameContainingIgnoreCase(productName);
        return productFound.isPresent();
    }

    private Product productFromDto(ProductDto productDto) {
        return Product.builder()
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .stock(productDto.getStock())
                .unitPrice(productDto.getUnitPrice())
                .status(productDto.isStatus())
                .build();
    }
}
