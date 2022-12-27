package dev.applaudostudios.applaudofinalproject.entity;

import dev.applaudostudios.applaudofinalproject.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "payment_type")
@Data
@AllArgsConstructor
@Builder
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Payment Type is required.")
    @Size(max = 20, message = "Max value is 20 characters")
    private String type;

    @OneToOne(mappedBy = "type")
    private Payment payment;

    public PaymentType() {}
}
