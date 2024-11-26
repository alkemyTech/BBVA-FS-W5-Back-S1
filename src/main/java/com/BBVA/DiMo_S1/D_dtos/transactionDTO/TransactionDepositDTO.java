package com.BBVA.DiMo_S1.D_dtos.transactionDTO;


import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDepositDTO {

    private Double amount;
    private String description;
    private CurrencyType currencyType;

}
