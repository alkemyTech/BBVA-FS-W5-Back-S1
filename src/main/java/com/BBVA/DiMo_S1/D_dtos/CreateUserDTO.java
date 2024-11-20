package com.BBVA.DiMo_S1.D_dtos;

import com.BBVA.DiMo_S1.D_models.Role;
import lombok.Data;

@Data
public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long roleId=2L;
}
