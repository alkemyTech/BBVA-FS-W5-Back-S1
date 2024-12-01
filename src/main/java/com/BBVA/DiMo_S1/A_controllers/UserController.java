package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.UserServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @Autowired
    AccountServiceImplementation accountServiceImplementation;

    @Autowired
    AuthServiceImplementation authServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- softDelete de un User de la BD.
    @DeleteMapping("/{idUser}")
    public ResponseEntity<String> softDelete(HttpServletRequest request, @Valid @PathVariable @NotNull
            (message = "El ID del User no puede ser nulo.") long idUser) throws CustomException {

        jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        userServiceImplementation.softDelete(request, idUser);

        return ResponseEntity.ok().body("User con ID = " + idUser + " eliminado con éxito!");
    }

    //3- Listar los Users presentes en el sistema.
    //-----------------------------------------------------------------------------------------------------------
    @GetMapping()
    public ResponseEntity<Page<FullUserDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) throws CustomException {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        String toUpperCaseRole = userSecurityDTO.getRole();

        if (toUpperCaseRole.toUpperCase().equals("ADMIN")) {
            return ResponseEntity.ok(userServiceImplementation.getAll(PageRequest.of(page, size)));
        } else {
            throw new CustomException(HttpStatus.CONFLICT, "Acceso denegado no es usuario administrador");
        }
    }
    //-----------------------------------------------------------------------------------------------------------

    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<AccountDTO>> getAccountByUser(@PathVariable long userId) {

        List<AccountDTO> listAccount = userServiceImplementation.listarCuentasPorUsuario(userId);

        return ResponseEntity.ok(listAccount);
    }

    @GetMapping("/usersProfile")
    public ResponseEntity<UserDTO> userDetail(HttpServletRequest request) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        return ResponseEntity.ok(userServiceImplementation.userDetail(userSecurityDTO.getId()));
    }

    @GetMapping("/usersProfile/{idUser}")
    public ResponseEntity<UserDTO> findUserDetail(HttpServletRequest request, @PathVariable Long idUser) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        if (userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {
            return ResponseEntity.ok(userServiceImplementation.userDetail(idUser));
        } else {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }
    }

    @PatchMapping("/update/{idUser}")
    public ResponseEntity<UpdateUserDTO> userUpdateAdmin(HttpServletRequest request, @RequestBody UpdateUserDTO
            createUserDTO, @PathVariable Long idUser) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        if (userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {
            createUserDTO = userServiceImplementation.updateUser(request, idUser, createUserDTO);
            return ResponseEntity.ok(createUserDTO);
        } else {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.CREDENCIALES_INVALIDAS);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateUserDTO> userUpdate(HttpServletRequest request, @RequestBody UpdateUserDTO
            createUserDTO) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        Long idUser = userSecurityDTO.getId();
        createUserDTO = userServiceImplementation.updateUser(request, idUser, createUserDTO);
        return ResponseEntity.ok(createUserDTO);
    }
}