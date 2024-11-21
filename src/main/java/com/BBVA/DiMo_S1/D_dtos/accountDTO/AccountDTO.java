package com.BBVA.DiMo_S1.D_dtos.accountDTO;

import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountDTO {
    private CurrencyType currency;
    private double transactionLimit;
    private double balance ;

    public AccountDTO(CurrencyType currency) {
        this.currency = currency;
        this.balance = 0;
        this.transactionLimit = (currency == CurrencyType.ARS) ? 300000 : 1000;
    }




}
