package com.BBVA.DiMo_S1.D_dtos;

import com.BBVA.DiMo_S1.D_models.Role;
import com.BBVA.DiMo_S1.D_models.User;
import lombok.Data;

import java.time.LocalDateTime;


//Nombre, Apellido, Email, Contraseña
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime softDelete;
    private Long role;

    public UserDTO fromUser(final User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.creationDate = user.getCreationDate();
        this.updateDate = user.getUpdateDate();
        this.role = 2L;//user.getRole();
        return this;
    }
}
