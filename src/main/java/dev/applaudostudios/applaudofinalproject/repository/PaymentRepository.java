package dev.applaudostudios.applaudofinalproject.repository;
import dev.applaudostudios.applaudofinalproject.models.Payment;
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
    Optional<Payment> findPaymentByCcNumber(String ccNumber);
    List<Payment> findAllByUserSidAndStatusIsTrue(String sid);
    Optional<Payment> findByIdAndStatusIsTrueAndUserSid(Long id, String sid);
    List<Payment> findAllByUserSidAndStatusIsTrue(String sid, Pageable pageable);
}
