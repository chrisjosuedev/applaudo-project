package dev.applaudostudios.applaudofinalproject.service.imp;


import dev.applaudostudios.applaudofinalproject.dto.entities.PaymentDto;
import dev.applaudostudios.applaudofinalproject.entity.Payment;
import dev.applaudostudios.applaudofinalproject.entity.PaymentType;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.repository.PaymentRepository;
import dev.applaudostudios.applaudofinalproject.repository.PaymentTypeRepository;
import dev.applaudostudios.applaudofinalproject.service.IPaymentService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.InfoCredential;
import dev.applaudostudios.applaudofinalproject.utils.helpers.ObjectNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private InfoCredential infoCredential;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectNull objectNull;

    @Override
    public List<Payment> findAll(Integer from, Integer limit, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);

        List<Payment> allPayments = paymentRepository.findAllByUserSidAndStatusIsTrue(currentLoggedUser.getSid());

        if (limit == null || from == null) {
            return allPayments;
        }

        if (limit < 0 || from < 0) {
            throw new MyBusinessException("Limit and From must be greater than zero.", HttpStatus.BAD_REQUEST);
        }

        allPayments = paymentRepository.findAllByUserSidAndStatusIsTrue(
                currentLoggedUser.getSid(),
                PageRequest.of(from, limit));

        return allPayments;
    }

    @Override
    public Payment createPayment(PaymentDto paymentDto, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
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

    @Override
    public Payment updatePayment(Long id, PaymentDto paymentDto, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        PaymentType typeFound = findPaymentType(paymentDto.getType().getId());
        Payment paymentFound = findUserPayment(id, currentLoggedUser);


        Optional<Payment> currentDefaultPayment = paymentRepository.findPayment(currentLoggedUser.getSid());
        if(paymentDto.isDefault() && currentDefaultPayment.isPresent()) {
            currentDefaultPayment.get().setDefault(false);
            paymentRepository.save(currentDefaultPayment.get());
        }

        paymentFound.setCcExpirationDate(paymentDto.getCcExpirationDate());
        paymentFound.setProvider(paymentDto.getProvider());
        paymentFound.setCcNumber(paymentDto.getCcNumber());
        paymentFound.setDefault(paymentFound.isDefault());
        paymentFound.setType(typeFound);
        paymentRepository.save(paymentFound);

        return paymentFound;
    }

    @Override
    public Payment findPaymentById(Long id, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        return findUserPayment(id, currentLoggedUser);
    }

    @Override
    public Object deletePayment(Long id, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        Payment paymentFound = findUserPayment(id, currentLoggedUser);

        paymentFound.setStatus(false);
        paymentRepository.save(paymentFound);

        return objectNull.getObjectNull();
    }

    private Payment findUserPayment(Long id, User loggedUser) {
        Optional<Payment> paymentFound = paymentRepository.findByIdAndStatusIsTrueAndUserSid(id, loggedUser.getSid());

        if (paymentFound.isEmpty()) {
            throw new MyBusinessException("Current user doesn't have an address with given id.", HttpStatus.FORBIDDEN);
        }

        return paymentFound.get();
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
