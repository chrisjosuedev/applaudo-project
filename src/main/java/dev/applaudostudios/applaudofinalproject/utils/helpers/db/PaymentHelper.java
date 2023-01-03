package dev.applaudostudios.applaudofinalproject.utils.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.models.Payment;
import dev.applaudostudios.applaudofinalproject.models.PaymentType;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.PaymentRepository;
import dev.applaudostudios.applaudofinalproject.repository.PaymentTypeRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentHelper {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    public Payment findUserPayment(Long id, User loggedUser) {
        Optional<Payment> paymentFound = paymentRepository.findByIdAndStatusIsTrueAndUserSid(id, loggedUser.getSid());

        if (paymentFound.isEmpty()) {
            throw new MyBusinessException("Current user doesn't have an payment with given id.", HttpStatus.FORBIDDEN);
        }

        return paymentFound.get();
    }

    public boolean findCcPayment(String ccNumber) {
        Optional<Payment> paymentFound = paymentRepository.findPaymentByCcNumber(ccNumber);
        return paymentFound.isPresent();
    }

    public Payment findPayment(User loggedUser) {
        Optional<Payment> paymentFound = paymentRepository.findPayment(loggedUser.getSid());

        if (paymentFound.isEmpty()) {
            throw new MyBusinessException("Current user doesn't have a default payment method.", HttpStatus.FORBIDDEN);
        }

        return paymentFound.get();
    }

    public PaymentType findPaymentType(Long id) {
        Optional<PaymentType> typeFound = paymentTypeRepository.findById(id);

        if (typeFound.isEmpty()) {
            throw new MyBusinessException("Payment Type is invalid.", HttpStatus.BAD_REQUEST);
        }

        return typeFound.get();
    }

    public Payment paymentFromDto(PaymentDto paymentDto) {
        return Payment.builder()
                .ccExpirationDate(paymentDto.getCcExpirationDate())
                .ccNumber(paymentDto.getCcNumber())
                .provider(paymentDto.getProvider())
                .status(true)
                .isDefault(paymentDto.isDefault())
                .user(paymentDto.getUser())
                .type(paymentDto.getType())
                .build();
    }
}
