package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductNameContainingIgnoreCase(String productName);
}
