package com.BBVA.DiMo_S1.D_models;

import com.BBVA.DiMo_S1.D_models.Enums.enumCurrency;
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
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="CBU", unique = true)
    @NotNull
    private Long cbu;

    @Column(name = "currency")
    @NotNull(message = "La divisa no debe estar nula")
    private enumCurrency currency;

    @Column(name = "transaction_limit")
    @NotNull(message = "El limite de transaccion no debe estar nulo")
    private double transactionLimit;

    @Column(name = "balance")
    @NotNull(message = "El balance no debe estar nulo")
    private double balance;

    @Column(updatable = false, name = "create_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "soft_delete")
    private LocalDateTime softDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;
}
