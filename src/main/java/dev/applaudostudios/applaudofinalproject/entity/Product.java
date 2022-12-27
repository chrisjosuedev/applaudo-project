package dev.applaudostudios.applaudofinalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    @Size(max = 50, message = "Product name must have 50 characters max")
    @NotBlank(message = "Product Name is required")
    private String productName;

    @Column
    @NotBlank(message = "Description is required")
    private String description;

    @Column
    @Min(value = 1, message = "Min value to add is 1 product")
    @NotBlank(message = "Stock is required")
    private int stock;

    @Column(name = "unit_price")
    @Positive(message = "Price must be greater than 0")
    @NotBlank(message = "Price is required")
    private double unitPrice;

    @Column
    private boolean status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<CartItemSession> cartItemSessions = new HashSet<>();

    public Product(){}

}
