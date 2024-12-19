package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleUserDTO {
    private Long role;

    public UpdateRoleUserDTO(User user){

        this.role= user.getRole().getId();
    }
}
