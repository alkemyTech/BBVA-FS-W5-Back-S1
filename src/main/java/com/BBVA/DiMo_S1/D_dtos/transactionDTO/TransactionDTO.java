package com.BBVA.DiMo_S1.D_dtos.transactionDTO;

import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
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
public class TransactionDTO {
    private Long id;
    private Double amount;
    private CurrencyType currencyType;
    private TransactionType type;
    private String description;
    private LocalDateTime transactionDate;
    private String cuenta;
    private String titular;
    private String cuentaDestino;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.currencyType = transaction.getAccount().getCurrency();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.cuenta = transaction.getAccount().getCbu();
        this.titular = (transaction.getAccount().getUser().getFirstName() + " "
                + transaction.getAccount().getUser().getLastName());
        if (transaction.getType().equals(TransactionType.payment) && transaction.getAccountDestino() != null) {
            this.cuentaDestino = transaction.getAccountDestino().getCbu();
        } else {
            this.cuentaDestino = null;
        }
    }
}
