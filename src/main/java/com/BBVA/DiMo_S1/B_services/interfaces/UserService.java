package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_models.User;

public interface UserService {

    //1- softDelete de un User de la BD.
    void softDelete (final long idUser);
    UserDTO create(final CreateUserDTO createUserDTO);
    User findById(Long id);

}
