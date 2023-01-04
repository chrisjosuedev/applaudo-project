package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.payments.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {}
