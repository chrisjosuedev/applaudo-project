package dev.applaudostudios.applaudofinalproject.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "orders_detail")
@Data
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Min(value = 1, message = "Value must be greater than 0")
    private int quantity;

    @Column(name = "unit_price")
    private double price;

    @JsonManagedReference
    @ManyToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public OrderDetail(){}
}
