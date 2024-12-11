package com.BBVA.DiMo_S1.D_dtos.transactionDTO;

import com.BBVA.DiMo_S1.D_models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SimpleTransactionDTO {

    private Double amount;
    private String description;
    private String CBU;

    public SimpleTransactionDTO(Transaction transaction) {

        this.amount = transaction.getAmount();

        this.description = transaction.getDescription();

        this.CBU = transaction.getAccount().getCbu();
    }
}
