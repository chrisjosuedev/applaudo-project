package dev.applaudostudios.applaudofinalproject.repository;
import dev.applaudostudios.applaudofinalproject.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT u FROM Payment u WHERE u.isDefault = true AND u.user.sid = ?1")
    Optional<Payment> findPayment(String sid);

    List<Payment> findAllByUserSid(String sid);

    List<Payment> findAllByUserSid(String sid, Pageable pageable);
}
