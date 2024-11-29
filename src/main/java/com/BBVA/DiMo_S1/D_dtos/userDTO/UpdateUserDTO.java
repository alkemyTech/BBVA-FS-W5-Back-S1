package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String password;

}
