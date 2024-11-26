package com.BBVA.DiMo_S1.D_dtos.transactionDTO;

import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TransactionCompletaDTO {
    private Double amount;
    private TransactionType type;
    private String description;
    private LocalDateTime transactionDate;
    private Double balance;
    private String cbu;
    private Double transactionLimit;



    public TransactionCompletaDTO(Transaction transaction){
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.balance = transaction.getAccount().getBalance();
        this.cbu = transaction.getAccount().getCbu();
        this.transactionLimit = transaction.getAccount().getTransactionLimit();

    }
}
