package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;

import java.util.Map;


public interface AuthService {

    //1- Registro de User en el sistema.
    ShowCreatedUserDTO createUser (final CreateUserDTO createUserDTO);

    //2- Login de User en el sistema.
    ShowCreatedUserDTO login (final String username, final String password);
}
