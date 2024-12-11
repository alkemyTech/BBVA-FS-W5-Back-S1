package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {

    //1- Agregar a un usuario a la lista de usuarios favoritos.
    UserDTO addUserToFavList(HttpServletRequest request, Long idUser);

    //2- Actualizar datos de perfil como usuario autenticado.
    UpdateUserDTO updateUser(HttpServletRequest request, UpdateUserDTO updateUserDTO);

    //3- Actualizar datos de perfil de un determinado usuario como administrador.
    UpdateUserDTO updateUserAdmin(HttpServletRequest request, Long idUser, UpdateUserDTO updateUserDTO);

    //4- Obtener datos de perfil como usuario autenticado.
    UserDTO userDetail(HttpServletRequest request);

    //5- Mostrar lista de favoritos del usuario autenticado.
    Set<UserDTO> showFavList(HttpServletRequest request);

    //6- Paginar usuarios presentes en el sistema.
    Page<FullUserDto> getAll(Pageable pageable, HttpServletRequest request);

    //7- Obtener datos de perfil de un determinado usuario como adminstrador.
    UserDTO userDetailAdmin(HttpServletRequest request, Long idUser);

    //8- Baja de un usuario del sistema.
    void softDeleteByUser(HttpServletRequest request);

    //9- Dar de baja del sistema a un usuario con rol de administrador.
    void softDeleteByAdmin(HttpServletRequest request, Long idUser);

}
