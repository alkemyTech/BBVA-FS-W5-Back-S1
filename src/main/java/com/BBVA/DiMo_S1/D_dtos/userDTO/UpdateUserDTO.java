package com.BBVA.DiMo_S1.D_dtos.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String password;

}
