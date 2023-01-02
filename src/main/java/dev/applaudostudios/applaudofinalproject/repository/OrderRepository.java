package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUserSid(Long id, String sid);
    List<Order> findAllByUserSid(String sid);
}
