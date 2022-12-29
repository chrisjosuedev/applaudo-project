package dev.applaudostudios.applaudofinalproject.service.imp;


import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.entity.Payment;
import dev.applaudostudios.applaudofinalproject.entity.PaymentType;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.repository.PaymentRepository;
import dev.applaudostudios.applaudofinalproject.repository.PaymentTypeRepository;
import dev.applaudostudios.applaudofinalproject.repository.UserRepository;
import dev.applaudostudios.applaudofinalproject.service.IPaymentService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> findAll(Integer from, Integer limit, String username) {
        User currentLoggedUser = findUserInSession(username);

        List<Payment> allPayments = paymentRepository.findAllByUserSid(currentLoggedUser.getSid());

        if (limit == null || from == null) {
            return allPayments;
        }

        if (limit < 0 || from < 0) {
            throw new MyBusinessException("Limit and From must be greater than zero.", HttpStatus.BAD_REQUEST);
        }

        allPayments = paymentRepository.findAllByUserSid(
                currentLoggedUser.getSid(),
                PageRequest.of(from, limit));

        return allPayments;
    }

    @Override
    public Payment createPayment(PaymentDto paymentDto, String username) {
        User currentLoggedUser = findUserInSession(username);
        PaymentType typeFound = findPaymentType(paymentDto.getType().getId());

        paymentDto.setUser(currentLoggedUser);
        paymentDto.setType(typeFound);

        Payment newPayment = paymentFromDto(paymentDto);

        Optional<Payment> currentDefaultPayment = paymentRepository.findPayment(currentLoggedUser.getSid());
        if(newPayment.isDefault() && currentDefaultPayment.isPresent()) {
            currentDefaultPayment.get().setDefault(false);
            paymentRepository.save(currentDefaultPayment.get());
        }

        paymentRepository.save(newPayment);

        return newPayment;
    }

    private User findUserInSession(String username) {
        Optional<User> currentLoggedUser = userRepository.findByUsername(username);
        if (currentLoggedUser.isEmpty()) {
            throw new MyBusinessException("Current user doesn't exists or session is invalid.", HttpStatus.FORBIDDEN);
        }
        return currentLoggedUser.get();
    }

    private PaymentType findPaymentType(Long id) {
        Optional<PaymentType> typeFound = paymentTypeRepository.findById(id);

        if (typeFound.isEmpty()) {
            throw new MyBusinessException("Payment Type is invalid.", HttpStatus.BAD_REQUEST);
        }

        return typeFound.get();
    }

    private Payment paymentFromDto(PaymentDto paymentDto) {
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
