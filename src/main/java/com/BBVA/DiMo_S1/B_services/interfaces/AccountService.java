package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowCreatedAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowUpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.UpdateAccountDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.servlet.http.HttpServletRequest;

public interface AccountService {

    //1- Creación de una Cuenta.
    ShowCreatedAccountDTO createAccount(final long idUser, final CurrencyType currencyType);

    //2- softDelete de una Cuenta.
    void softDelete(final HttpServletRequest request, final long idAccount);

    BalanceDto obtainBalance(HttpServletRequest request);

    Account getAccountByEmail(String email);

    //3- Actualizar Account del User
    ShowUpdateAccountDTO updateAccount(HttpServletRequest request, UpdateAccountDTO updateAccountDTO, String cbu);
}
