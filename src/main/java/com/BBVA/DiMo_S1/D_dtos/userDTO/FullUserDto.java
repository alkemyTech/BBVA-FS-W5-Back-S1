package com.BBVA.DiMo_S1.D_dtos.userDTO;


import com.BBVA.DiMo_S1.D_models.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FullUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime softDelete;
    private String role;

    public FullUserDto (User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.creationDate = user.getCreationDate();
        this.updateDate = user.getUpdateDate();
        this.softDelete = user.getSoftDelete();
        this.role = user.getRole().getName();
    }
}