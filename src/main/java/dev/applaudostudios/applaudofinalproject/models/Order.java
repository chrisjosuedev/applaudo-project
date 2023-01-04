package dev.applaudostudios.applaudofinalproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @CreatedDate
    private Date date;

    @Column(name = "tracking_num")
    private String trackNum;

    @Column
    private boolean status;

    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderDetail> orderDetail;

    public Order() {}

}
