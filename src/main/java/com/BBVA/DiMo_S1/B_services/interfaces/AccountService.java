package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.CreateAccountDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;

public interface AccountService {
    //1- softDelete de una Account de la BD.
    void softDelete (final long idAccount);

    //2- createAccount
    AccountDTO createAccount( final Long idUsuario, CurrencyType currencyType);



    Account getAccountByEmail(String email);
}
