package dev.applaudostudios.applaudofinalproject.models.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.applaudostudios.applaudofinalproject.models.Order;
import dev.applaudostudios.applaudofinalproject.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    @JsonIgnore
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PaymentType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Order> order = new ArrayList<>();

    public Payment(){}

}
