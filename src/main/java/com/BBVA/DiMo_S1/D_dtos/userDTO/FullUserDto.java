package com.BBVA.DiMo_S1.D_dtos.userDTO;


import com.BBVA.DiMo_S1.D_models.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FullUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private String role;

    public FullUserDto (User user) {

        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.creationDate = user.getCreationDate();
        this.updateDate = user.getUpdateDate();
        this.role = user.getRole().getName();
    }
}