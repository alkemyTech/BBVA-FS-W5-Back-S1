package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.User;
import lombok.Data;

@Data
public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public void guardarDTO (final User user) {
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setEmail(this.getEmail());
        user.setPassword(this.getPassword());
    }
}
