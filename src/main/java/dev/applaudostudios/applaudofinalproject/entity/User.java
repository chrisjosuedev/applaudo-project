package dev.applaudostudios.applaudofinalproject.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String uuid;

    @Column(name = "first_name")
    @NotBlank(message = "First name is required.")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Column
    private String username;

    @Column
    @Email(message = "Email must be in a valid format")
    private String email;

    public User() {}
}
