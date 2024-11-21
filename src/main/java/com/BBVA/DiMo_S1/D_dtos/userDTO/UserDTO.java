package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.User;
import lombok.Data;

import java.time.LocalDateTime;


//Nombre, Apellido, Email, Contraseña
@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime creationDate;

    public UserDTO fromUser(final User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.creationDate = user.getCreationDate();
        return this;
    }
}