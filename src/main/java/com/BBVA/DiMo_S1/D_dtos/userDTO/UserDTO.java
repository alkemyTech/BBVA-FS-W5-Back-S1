package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


//Nombre, Apellido, Email, Contraseña
@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime creationDate;

    public UserDTO(final User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.creationDate = user.getCreationDate();

    }
}
