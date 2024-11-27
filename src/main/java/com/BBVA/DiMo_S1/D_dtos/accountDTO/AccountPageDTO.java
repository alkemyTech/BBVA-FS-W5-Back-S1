package com.BBVA.DiMo_S1.D_dtos.accountDTO;

import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;

import java.time.LocalDateTime;

public class AccountPageDTO {
    private String titular;
    private String cbu;
    private CurrencyType currency;
    private double transactionLimit;
    private double balance;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    public AccountPageDTO(Account account) {
        this.titular = (account.getUser().getFirstName());
        this.cbu = account.getCbu();
        this.currency = account.getCurrency();
        this.transactionLimit = account.getTransactionLimit();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.updateDate = account.getUpdateDate();
    }
}
