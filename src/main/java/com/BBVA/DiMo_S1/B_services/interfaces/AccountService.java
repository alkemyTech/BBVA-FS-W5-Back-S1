package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_models.Account;

public interface AccountService {
    //1- softDelete de una Account de la BD.
    void softDelete (final long idAccount);

    //2- createAccount
    Account createAccount(Account account);

    Account getAccountByEmail(String email);
}
