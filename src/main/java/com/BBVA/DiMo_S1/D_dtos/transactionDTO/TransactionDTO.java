package com.BBVA.DiMo_S1.D_dtos.transactionDTO;

import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    @JsonIgnore
    private Long id;

    private Double amount;
    private TransactionType type;
    private String description;
    private LocalDateTime transactionDate;
    private String cuentaDestino;

    public TransactionDTO (Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.cuentaDestino = transaction.getAccount().getCbu();
    }
}
