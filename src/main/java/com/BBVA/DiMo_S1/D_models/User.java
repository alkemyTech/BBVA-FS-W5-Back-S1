package com.BBVA.DiMo_S1.D_models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El nombre del Usuario no puede ser nulo.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "El apellido del Usuario no puede ser nulo.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull(message = "El email no puede ser nulo.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$",
            message = "El email debe tener un formato válido."
    )
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "La contraseña no puede ser nula.")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = true, updatable = true, insertable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "soft_delete", nullable = true)
    private LocalDateTime softDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> favoritos;
}