package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    //1- softDelete de un User de la BD.
    void softDelete (HttpServletRequest request, final long idUser);

    UserDTO create(final CreateUserDTO createUserDTO);
    User findById(Long id);
    List<FullUserDto>getAll();
    List<AccountDTO> listarCuentasPorUsuario(long userId)throws CustomException;

}
