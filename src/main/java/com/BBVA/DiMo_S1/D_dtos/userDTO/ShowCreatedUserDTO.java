package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ShowCreatedUserDTO {

    private String firstName;
    private String lastName;
    private String token;

    public ShowCreatedUserDTO (final User user, String token) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.token = token;
    }
}
