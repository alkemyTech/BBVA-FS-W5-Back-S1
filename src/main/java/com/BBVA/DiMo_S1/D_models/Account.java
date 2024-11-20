package com.BBVA.DiMo_S1.D_models;

import com.BBVA.DiMo_S1.D_models.Enums.enumCurrency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

//Creacion de la tabla de Account

@Entity
@Table(name = "Accounts")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency")
    @NotNull(message = "La divisa no debe estar nula")
    private enumCurrency currency;

    @Column(name = "transaction_limit")
    @NotNull(message = "El limite de transaccion no debe estar nulo")
    private double transactionLimit;

    @Column(name = "balance")
    @NotNull(message = "El balance no debe estar nulo")
    private double balance;

    @Column(name = "user_id")
    private int userId;

    @Column(updatable = false, name = "create_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime updateDate;

    @Column(name = "soft_delete")
    private LocalDateTime softDelete;

     //@PrePersist
    // protected void onCreate(){
    // this.creationDate = new Date();}

    //@PreUpdate protected void onUpdate(){
    // this.updateDate = new Date();}


}
