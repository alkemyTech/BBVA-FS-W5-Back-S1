package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.UserServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "F- Usuarios")
public class UserController {

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- Agregar a un usuario a la lista de usuarios favoritos.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Agregar a un usuario a lista de usuarios favoritos", description = "Endpoint para agregar a un " +
            "usuario a la lista de usuarios favoritos. El endpoint permite al usuario autenticado agregar a un usuario " +
            "a su lista de usuarios favoritos buscandolo por ID." +
            "\n\nConsideraciones:" +
            "\n- El usuario que se desee agregar en la lista debe estar presente en el sistema"
    )
    @PostMapping("favList/{idUser}")
    public ResponseEntity<UserDTO> addUserToFavList(HttpServletRequest request, @PathVariable Long idUser) {

        return ResponseEntity.ok(userServiceImplementation.addUserToFavList(request, idUser));
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Actualizar datos de perfil como usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Actualizar datos del perfil", description = "Endpoint para actualizar los datos del perfil. " +
            "El endpoint permite al usuario autenticado actualizar los datos de su perfil." +
            "\n\nConsideraciones:" +
            "\n- Los unicos datos que puede modificar el usuario es el nombre, apellido y contraseña." +
            "\n- La contraseña debe tener entre 6 y 20 caracteres."
    )
    @PatchMapping("/update")
    public ResponseEntity<UpdateUserDTO> userUpdate(HttpServletRequest request, @RequestBody UpdateUserDTO
            createUserDTO) throws CustomException {
        return ResponseEntity.ok(userServiceImplementation.updateUser(request, createUserDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Actualizar datos de perfil como usuario administrador.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Actualizar datos del perfil", description = "Endpoint para actualizar los datos del perfil. " +
            "El endpoint permite al usuario autenticado con rol de administrador actualizar los datos del perfil de un " +
            "determinado usuario buscandolo por ID." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador." +
            "\n- El ID debe corresponder a un usuario presente en el sistema." +
            "\n- Los unicos datos que puede modificar el usuario es el nombre, apellido y contraseña." +
            "\n- La contraseña debe tener entre 6 y 20 caracteres."
    )
    @PatchMapping("/admin/update/{idUser}")
    public ResponseEntity<UpdateUserDTO> userUpdateAdmin(HttpServletRequest request, @PathVariable Long idUser, @RequestBody UpdateUserDTO
            createUserDTO) {
        return ResponseEntity.ok(userServiceImplementation.updateUserAdmin(request, idUser, createUserDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

    //4- Obtener datos de perfil como usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener datos de perfil", description = "Endpoint para obtener los datos del perfil. " +
            "El endpoint permite al usuario autenticado obtener los datos de su perfil."
    )
    @GetMapping("/userProfile")
    public ResponseEntity<UserDTO> userDetail(HttpServletRequest request) {
        return ResponseEntity.ok(userServiceImplementation.userDetail(request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //5- Mostrar lista de favoritos del usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Mostrar lista de usuarios favoritos", description = "Endpoint para mostrar la lista de usuarios " +
            "favoritos del usuario autenticado. El endpoint permite al usuario autenticado mostrar la lista de sus usuarios " +
            "favoritos."
    )
    @GetMapping("/favList")
    public ResponseEntity<Set<UserDTO>> showFavList(HttpServletRequest request) {

        return ResponseEntity.ok(userServiceImplementation.showFavList(request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //6- Listar los usuarios presentes en el sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener usuarios", description = "Endpoint para obtener los usuarios presentes en el sistema. " +
            "El endpoint permite al usuario autenticado con rol de administrador obtener los usuarios presentes en el sistema." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador."
    )
    @GetMapping("/admin")
    public ResponseEntity<Page<FullUserDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(userServiceImplementation.getAll(PageRequest.of(page, size), request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //7- Obtener datos de perfil de un determinado usuario como adminstrador.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener datos de perfil", description = "Endpoint para obtener los datos del perfil. " +
            "El endpoint permite al usuario autenticado con rol de administrador obtener los datos del perfil " +
            "de un usuario buscandolo por ID." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador." +
            "\n- El ID debe corresponder a un usuario presente en el sistema."
    )
    @GetMapping("admin/userProfile/{idUser}")
    public ResponseEntity<UserDTO> userDetailAdmin(HttpServletRequest request, @PathVariable Long idUser) {
        return ResponseEntity.ok(userServiceImplementation.userDetailAdmin(request, idUser));
    }
    //-----------------------------------------------------------------------------------------------------------

    //8- Darse de baja del sistema como usuario.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Darse de baja del sistema", description = "Endpoint para darse de baja del sistema. " +
            "El endpoint permite al usuario autenticado darse de baja del sistema y desactivar sus cuentas." +
            "\n\nConsideraciones:" +
            "\n- Una vez dado de baja, el usuario no podrá iniciar sesión nuevamente" +
            "\n- Para poder regresar al sistema, debe activar su cuenta nuevamente." +
            "\n- Sus cuentas, quedarán inhabilitadas. Las mismas no podrán recibir dinero de otra cuenta."
    )
    @DeleteMapping()
    public ResponseEntity<String> softDeleteByUser(HttpServletRequest request) {

        userServiceImplementation.softDeleteByUser(request);

        return ResponseEntity.ok().body("Fuiste dado de baja con éxito!");
    }
    //-----------------------------------------------------------------------------------------------------------

    //9- Dar de baja del sistema a un usuario con rol de administrador.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Dar de baja del sistema a un usuario", description = "Endpoint para dar de baja del sistema " +
            "a un usuario. El endpoint permite al usuario autenticado con rol de administrador dar de baja a un usuario " +
            "presente en el sistema y desactivar sus cuentas." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador" +
            "\n- Si el usuario ya fue dado de baja, no se va a poder eliminarlo nuevamente" +
            "\n- Una vez dado de baja, el usuario no podrá iniciar sesión nuevamente" +
            "\n- Para poder regresar al sistema, debe activar su cuenta nuevamente." +
            "\n- Sus cuentas, quedarán inhabilitadas. Las mismas no podrán recibir dinero de otra cuenta."
    )
    @DeleteMapping("admin/{idUser}")
    public ResponseEntity<String> softDeleteByAdmin(HttpServletRequest request, @PathVariable Long idUser) {

        userServiceImplementation.softDeleteByAdmin(request, idUser);

        return ResponseEntity.ok().body("El usuario con ID = " + idUser + " fue dado de baja con éxito!");
    }
    //-----------------------------------------------------------------------------------------------------------
}