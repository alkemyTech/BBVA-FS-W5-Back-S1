package com.BBVA.DiMo_S1.D_models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull(message = "El nombre del Usuario no puede ser nulo.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "El apellido del Usuario no puede ser nulo.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull(message = "El email no puede ser nulo.")
    @Email(message = "El email debe tener un formato válido.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "La contraseña no puede ser nula.")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "soft_delete", nullable = true)
    private LocalDateTime softDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}