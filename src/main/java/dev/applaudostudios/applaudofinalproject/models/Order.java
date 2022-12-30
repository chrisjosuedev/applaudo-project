package dev.applaudostudios.applaudofinalproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @Column
    private Long id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date date;

    @Column(name = "tracking_num")
    private String trackNum;

    @Column
    private boolean status;

    @ManyToOne(optional = false)
    private User user;

    @OneToOne(mappedBy = "order")
    private OrderDetail orderDetail;

    public Order() {}

}
