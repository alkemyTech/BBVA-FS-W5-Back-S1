package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.LoginUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ReactivateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/auth")
@Tag(name = "A- Autenticación")
public class AuthController {

    @Autowired
    AuthServiceImplementation authServiceImplementation;

    //1- Registro de User en el Sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Registrarse en el Sistema", description = "Endpoint para darse de alta en el sistema. " +
            "El endpoint permite al usuario registrarse y le brinda el token de autorizacion para poder ingresar al " +
            "sistema. Además, al mismo, se le crea inicialmente una cuenta en ARS." +
            "\n\nConsideraciones:" +
            "\n- Los campos deben estar completos, ninguno puede ser nulo." +
            "\n- El formato del mail debe ser de la siguiente manera: (A-Z)@(A-Z).com" +
            "\n- La contraseña debe tener entre 6 y 20 caracteres.")
    @PostMapping("/register")
    public ResponseEntity<ShowCreatedUserDTO> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authServiceImplementation.createUser(createUserDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Login de User en el Sistema
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Ingresar al Sistema", description = "Endpoint para ingresar al sistema. " +
            "El endpoint permite al usuario loguearse en el sistema, validando las credenciales, y le brinda el token " +
            "de autorizacion para poder ingresar." +
            "\n\nConsideraciones:" +
            "\n\n- El usuario debe estar previamente registrado y las credenciales deben ser válidas.")
    @PostMapping("/login")
    public ResponseEntity<ShowCreatedUserDTO> login(@RequestBody LoginUserDTO loginUserDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authServiceImplementation.login(loginUserDTO.getEmail(),
                loginUserDTO.getPassword()));
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Reactivar User dado de baja.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Reactivar Usuario", description = "Endpoint para poder reactivar a un Usuario dado de baja. " +
            "El endpoint permite a un usuario darse de alta nuevamente en el sistema en caso de haberse dado de baja." +
            "\n\nConsideraciones:" +
            "\n\n- El usuario ya debe estar registrado y el email ingresado debe coincidir con el presente en el sistema.")
    @PostMapping("/reactivate")
    public ResponseEntity<String> reactivateUser(@RequestBody ReactivateUserDTO reactivateUserDTO) {
        authServiceImplementation.reactivateUser(reactivateUserDTO);
        return ResponseEntity.ok("Cuenta reactivada con éxito!");
    }
    //-----------------------------------------------------------------------------------------------------------
}
