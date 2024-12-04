package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    //1- Actualizar datos de perfil como usuario autenticado.
    UpdateUserDTO updateUser(HttpServletRequest request, UpdateUserDTO updateUserDTO);

    //2- Actualizar datos de perfil de un determinado usuario como administrador.
    UpdateUserDTO updateUserAdmin(HttpServletRequest request, Long idUser, UpdateUserDTO updateUserDTO);

    //3- Obtener datos de perfil como usuario autenticado.
    UserDTO userDetail(HttpServletRequest request);

    //4- Paginar usuarios presentes en el sistema.
    Page<FullUserDto> getAll(Pageable pageable, HttpServletRequest request);

    //5- Obtener datos de perfil de un determinado usuario como adminstrador.
    UserDTO userDetailAdmin(HttpServletRequest request, Long idUser);

    //6- Baja de un usuario del sistema.
    void softDeleteByUser(HttpServletRequest request);

    //7- Dar de baja del sistema a un usuario con rol de administrador.
    void softDeleteByAdmin(HttpServletRequest request, Long idUser);

}
