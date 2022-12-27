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
    private String sid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    @NotBlank(message = "Username is required.")
    private String username;

    @Column
    @Email(message = "Email must be in a valid format.")
    private String email;

    @Column
    private String telephone;

    public User() {}
}
