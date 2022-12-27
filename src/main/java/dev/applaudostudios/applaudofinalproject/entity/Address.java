package dev.applaudostudios.applaudofinalproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Street is required")
    private String street;

    @Column
    @NotBlank(message = "City is required")
    @Max(value = 80, message = "City Name only 50 characters allowed")
    private String city;

    @Column
    @NotBlank(message = "State is required")
    @Max(value = 50, message = "State Name only 50 characters allowed")
    private String state;

    @Column
    @NotBlank(message = "Country is required")
    @Max(value = 50, message = "Country Name only 50 characters allowed")
    private String country;

    @Column(name = "zip_code")
    @NotBlank(message = "Zip Code is required")
    @Size(min = 5, max = 5, message = "Zip code must have 5 characters")
    private String zipCode;

    @Column(name = "is_default")
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Address() {}
}
