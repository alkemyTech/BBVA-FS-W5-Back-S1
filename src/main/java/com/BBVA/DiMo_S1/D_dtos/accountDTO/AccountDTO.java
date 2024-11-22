package com.BBVA.DiMo_S1.D_dtos.accountDTO;

import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
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

public class AccountDTO {
    private CurrencyType currency;
    private double transactionLimit;
    private double balance ;
    private String cbu;
    private LocalDateTime creationDate;
    private UserDTO user;

    public AccountDTO(final Account account){
        this.currency = account.getCurrency();
        this.transactionLimit = account.getTransactionLimit();
        this.balance = account.getBalance();
        this.cbu = account.getCbu();
        this.creationDate = account.getCreationDate();
        this.user = new UserDTO(account.getUser());
    }
}