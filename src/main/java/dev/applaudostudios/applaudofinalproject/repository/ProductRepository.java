package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByStatusIsTrue();
    List<Product> findAllByStatusIsTrue(Pageable pageable);
    Optional<Product> findByProductNameContainingIgnoreCase(String productName);

    Optional<Product> findByIdAndStatusIsTrue(Long id);
}
