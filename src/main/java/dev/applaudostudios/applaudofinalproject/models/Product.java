package dev.applaudostudios.applaudofinalproject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

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
    @NotNull(message = "Stock is required")
    @Min(value = 1, message = "Min value to add is 1 product")
    private int stock;

    @Column(name = "unit_price")
    @NotNull(message = "Unit Price is required")
    @Positive(message = "Price must be greater than 0")
    private double unitPrice;

    @Column
    private boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItemSession> cartItemSessions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Product(){}

}
