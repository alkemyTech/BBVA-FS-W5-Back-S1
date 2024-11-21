package com.BBVA.DiMo_S1.D_dtos;

import com.BBVA.DiMo_S1.E_constants.Enums.enumCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountDTO {
    private enumCurrency currency;
    private double transactionLimit;
    private double balance ;

    public AccountDTO(enumCurrency currency) {
        this.currency = currency;
        this.balance = 0;
        this.transactionLimit = (currency == enumCurrency.ARS) ? 300000 : 1000;
    }




}
