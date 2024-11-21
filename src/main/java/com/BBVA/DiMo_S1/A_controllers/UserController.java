package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.UserServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserServiceImplementation userServiceImplementation;

    //1- softDelete de un User de la BD.
    @DeleteMapping("/{idUser}")
    public ResponseEntity<String> softDelete (@Valid @PathVariable @NotNull
            (message = "El ID del User no puede ser nulo.") long idUser) throws CustomException {

        userServiceImplementation.softDelete(idUser);

        return ResponseEntity.ok().body("User con ID = " + idUser + " eliminado con éxito!");
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody CreateUserDTO createUserDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceImplementation.create(createUserDTO));
    }
}
