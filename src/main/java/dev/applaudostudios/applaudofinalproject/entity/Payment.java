package dev.applaudostudios.applaudofinalproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cc_number")
    @Length(min = 16, max = 19, message = "Account number must be between 16-19 characters.")
    private String ccNumber;

    @Column
    @NotBlank(message = "Provider is required.")
    private String provider;

    @Column(name = "cc_expiration_date")
    @NotBlank(message = "Expiration date is required.")
    @Pattern(regexp = "(?:0?[1-9]|1[0-2])/[0-9]{2}", message = "Invalid expiration date")
    private String ccExpirationDate;

    @Column(name = "is_default")
    private boolean isDefault;

    @OneToOne(optional = false)
    private PaymentType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Payment(){}

}
