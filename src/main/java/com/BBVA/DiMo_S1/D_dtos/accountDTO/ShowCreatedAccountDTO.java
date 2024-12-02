package com.BBVA.DiMo_S1.D_dtos.accountDTO;

import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ShowCreatedAccountDTO {

    private String cbu;
    private double balance;
    private CurrencyType currency;
    private double transactionLimit;
    private LocalDateTime creationDate;

    public ShowCreatedAccountDTO (Account account) {
        this.currency = account.getCurrency();
        this.transactionLimit = account.getTransactionLimit();
        this.balance = account.getBalance();
        this.cbu = account.getCbu();
        this.creationDate = account.getCreationDate();
    }
}
