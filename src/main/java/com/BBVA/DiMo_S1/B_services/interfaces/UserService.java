package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    //1- softDelete de un User de la BD.
    void softDelete (HttpServletRequest request, final long idUser);
    User findById(Long id);
    Page<FullUserDto> getAll(Pageable pageable);
    List<AccountDTO> listarCuentasPorUsuario(long userId)throws CustomException;
    UserDTO userDetail(Long idUser);
    UpdateUserDTO updateUser(HttpServletRequest request,Long idUser, UpdateUserDTO updateUserDTO);

}
