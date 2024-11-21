package com.BBVA.DiMo_S1.D_models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "El nombre del Role no debe ser nulo.")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = true)
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
