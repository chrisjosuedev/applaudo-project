package dev.applaudostudios.applaudofinalproject.service;
import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.entity.Payment;

import java.util.List;

public interface IPaymentService {
    List<Payment> findAll(Integer from, Integer limit, String username);
    Payment createPayment(PaymentDto paymentDto, String username);
    Payment updatePayment(Long id, PaymentDto paymentDto, String username);
    Payment findPaymentById(Long id, String username);
    Object deletePayment(Long id, String username);
}
