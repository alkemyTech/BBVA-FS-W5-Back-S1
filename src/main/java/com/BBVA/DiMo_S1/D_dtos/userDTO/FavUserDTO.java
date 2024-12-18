package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FavUserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cuentaArs;
    private String cuentaUsd;

    public FavUserDTO(User user, List<Account> cuentas) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        if (cuentas.size() == 1) {
            this.cuentaArs = cuentas.get(0).getCbu();
            this.cuentaUsd = null;
        } else {
            this.cuentaArs = cuentas.get(0).getCbu();
            this.cuentaUsd = cuentas.get(1).getCbu();
        }
    }
}
