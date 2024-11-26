package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowUpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.UpdateAccountDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.servlet.http.HttpServletRequest;

public interface AccountService {
    //1- softDelete de una Account de la BD.
    void softDelete (final long idAccount);

    //2- createAccount
    AccountDTO createAccount(final Long idUsuario, CurrencyType currencyType);

    BalanceDto obtainBalance(HttpServletRequest request);

    Account getAccountByEmail(String email);

    //3- Actualizar Account del User
    ShowUpdateAccountDTO updateAccount (HttpServletRequest request, UpdateAccountDTO updateAccountDTO, String cbu);
}
