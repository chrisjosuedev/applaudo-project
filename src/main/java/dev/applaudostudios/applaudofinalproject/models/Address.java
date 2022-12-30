package dev.applaudostudios.applaudofinalproject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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
    @Size(min = 2, max = 50, message = "Invalid City name length")
    private String city;

    @Column
    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "Invalid City name length")
    private String state;

    @Column
    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Invalid City name length")
    private String country;

    @Column(name = "zip_code")
    @NotBlank(message = "Zip Code is required")
    @Size(min = 5, max = 5, message = "Zip code must have 5 characters")
    private String zipCode;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column
    @JsonIgnore
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_sid")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Order> order = new ArrayList<>();

    public Address() {}
}
