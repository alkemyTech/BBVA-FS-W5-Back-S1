package com.BBVA.DiMo_S1.D_models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table (name = "fixed_term_deposits")
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull(message = "El monto del Plazo Fijo no puede ser nulo.")
    @Column(name = "amount", nullable = false)
    private double amount;

    @NotNull(message = "El interes del Plazo Fijo no puede ser nulo.")
    @Column(name = "interest", nullable = false)
    private double interest;

    @Column(name = "creation_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "closing_date", nullable = false)
    private LocalDateTime closingDate;

    @Column(name = "settled", nullable = false)
    private boolean settled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
