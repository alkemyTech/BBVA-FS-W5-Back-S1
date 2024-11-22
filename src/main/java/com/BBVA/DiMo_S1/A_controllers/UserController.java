package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.UserServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> softDelete (HttpServletRequest request, @Valid @PathVariable @NotNull
            (message = "El ID del User no puede ser nulo.") long idUser) throws CustomException {

        jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        userServiceImplementation.softDelete(request, idUser);

        return ResponseEntity.ok().body("User con ID = " + idUser + " eliminado con éxito!");
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserDTO createUserDTO){

        UserDTO userDTO = userServiceImplementation.create(createUserDTO);
        accountServiceImplementation.createAccount(userDTO.getId(), CurrencyType.ARS);
        authServiceImplementation.login(userDTO.getEmail(), createUserDTO.getPassword());

        return ResponseEntity.ok(authServiceImplementation.login(userDTO.getEmail(), createUserDTO.getPassword()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<FullUserDto>>getAll(){
        return ResponseEntity.ok(userServiceImplementation.getAll());
    }
}
