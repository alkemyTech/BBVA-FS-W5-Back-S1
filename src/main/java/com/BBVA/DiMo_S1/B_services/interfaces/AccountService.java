package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.*;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    //1- Creación de una Cuenta.
    ShowCreatedAccountDTO createAccount(final long idUser, final CurrencyType currencyType);

    //2- Obtener balance de una Cuenta.
    BalanceDto obtainBalance(HttpServletRequest request);

    //3- Actualizar Account del User.
    ShowUpdateAccountDTO updateAccount(HttpServletRequest request, UpdateAccountDTO updateAccountDTO, String cbu);

    //4- softDelete de una Cuenta.
    void softDelete(final HttpServletRequest request, final long idAccount);

    //5- Paginado de Cuentas.
    Page<AccountPageDTO> getAll(Pageable pageable, HttpServletRequest request);

    Account getAccountByEmail(String email);

}
