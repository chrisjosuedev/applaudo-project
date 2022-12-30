package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.models.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll(Integer limit, Integer from);

    Product findById(Long id);

    Product createProduct(ProductDto productDto);

    Product updateProduct(Long id, ProductDto productDto);

    Object deleteProduct(Long id);
}
