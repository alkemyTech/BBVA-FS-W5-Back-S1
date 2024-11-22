package com.BBVA.DiMo_S1.D_models;

import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="CBU", unique = true)
    @NotNull
    private String cbu;

    @Column(name = "currency")
    @NotNull(message = "La divisa no debe ser nula.")
    private CurrencyType currency;

    @Column(name = "transaction_limit")
    @NotNull(message = "El limite de transaccion no debe ser nulo.")
    private double transactionLimit;

    @Column(name = "balance")
    @NotNull(message = "El balance no debe ser nulo.")
    private double balance;

    @Column(updatable = false, name = "creation_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = true)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "soft_delete")
    private LocalDateTime softDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ID", nullable = false)
    private User user;
}
