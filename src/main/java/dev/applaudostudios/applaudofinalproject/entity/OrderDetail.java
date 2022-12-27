package dev.applaudostudios.applaudofinalproject.entity;

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
    @Column
    private Long id;

    @Column
    @Min(value = 1, message = "Value must be greater than 0")
    private int quantity;

    @Column(name = "unit_price")
    private double price;

    @OneToOne(optional = false)
    private Order order;

    @OneToOne(optional = false)
    private Product product;

    public OrderDetail(){}
}
