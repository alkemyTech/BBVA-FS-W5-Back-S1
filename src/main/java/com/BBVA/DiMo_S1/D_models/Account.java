package com.BBVA.DiMo_S1.D_models;

import com.BBVA.DiMo_S1.D_models.Enums.enumCurrency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SoftDelete;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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

     @Column(updatable = false)
     @DateTimeFormat(pattern = "yyyy-mm-dd")
     private Date creationDate;

     @DateTimeFormat(pattern = "yyyy-mm-dd")
     private Date updateDate;

     @PrePersist
     protected void onCreate(){
         this.creationDate = new Date();
     }

     @PreUpdate
     protected void onUpdate(){
         this.updateDate = new Date();
     }


}
