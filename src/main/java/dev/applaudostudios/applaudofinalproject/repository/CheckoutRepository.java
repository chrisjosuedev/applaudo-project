package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.entity.CartItemSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckoutRepository extends JpaRepository<CartItemSession, Long> {
    @Query("SELECT p.productName as productName, p.unitPrice as unitPrice, (p.unitPrice * c.quantity) as subTotal, c.quantity as quantity FROM CartItemSession c JOIN c.product p WHERE c.user.sid = ?1")
    List<ICheckoutResponseDto> getCartInformation(String sid);

    Optional<CartItemSession> findByProductIdAndUserSid(Long id, String username);
}
