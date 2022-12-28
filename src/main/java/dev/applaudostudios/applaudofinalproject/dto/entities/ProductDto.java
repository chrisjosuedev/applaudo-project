package dev.applaudostudios.applaudofinalproject.dto.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
public class ProductDto {
    @Size(max = 50, message = "Product name must have 50 characters max")
    @NotBlank(message = "Product Name is required")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Min value to add is 1 product")
    @NotNull(message = "Stock is required")
    private int stock;

    @Positive(message = "Price must be greater than 0")
    @NotNull(message = "Unit Price is required")
    private double unitPrice;

    @AssertTrue(message = "Init status value must be True")
    private boolean status;
}
