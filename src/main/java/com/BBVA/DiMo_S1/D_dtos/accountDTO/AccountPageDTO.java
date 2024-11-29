package com.BBVA.DiMo_S1.D_dtos.accountDTO;



import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountPageDTO {
    private String titular;
    private String cbu;
    private CurrencyType currency;
    private double transactionLimit;
    private double balance;

    public AccountPageDTO(Account account) {
        this.titular = (account.getUser().getFirstName()+" "+account.getUser().getLastName());
        this.cbu = account.getCbu();
        this.currency = account.getCurrency();
        this.transactionLimit = account.getTransactionLimit();
        this.balance = account.getBalance();
    }
}
