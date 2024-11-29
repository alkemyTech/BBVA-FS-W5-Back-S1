package com.BBVA.DiMo_S1.D_dtos.accountDTO;


import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import lombok.Data;



@Data
public class CreateAccountDTO {

    private CurrencyType tipoDeCuenta;

    public void guardarDtoEnAccount (final Account account) {
        account.setCurrency(this.tipoDeCuenta);
    }
}
