package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.*;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {

    //1- Creación de una Cuenta.
    ShowCreatedAccountDTO createAccount(final long idUser, final CurrencyType currencyType);

    //2- Mostrar las cuentas pertenecientes al usuario autenticado.
    List<AccountPageDTO> getAccounts(HttpServletRequest request);

    //3- Obtener balance de una Cuenta.
    BalanceDto obtainBalance(HttpServletRequest request);

    //4- Actualizar Account del User.
    ShowUpdateAccountDTO updateAccount(HttpServletRequest request, UpdateAccountDTO updateAccountDTO, String cbu);

    //5- Mostrar las cuentas pertenecientes a un determinado usuario buscandolo por id.
    List<AccountPageDTO> getAccountsAdmin(HttpServletRequest request, Long idUser);

    //6- Paginado de Cuentas.
    Page<AccountPageDTO> getAll(Pageable pageable, HttpServletRequest request);

}
