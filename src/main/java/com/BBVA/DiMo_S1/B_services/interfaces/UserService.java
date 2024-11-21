package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.UserDTO;
import com.BBVA.DiMo_S1.D_models.User;

import java.util.List;

public interface UserService {

    //1- softDelete de un User de la BD.
    void softDelete (final long idUser);
    UserDTO create(final CreateUserDTO createUserDTO);
    User findById(Long id);
    List<UserDTO>getAll();
    UserDTO convertToDto(User user);

}
