package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.LoginUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticación", description = "auth-controller")
@Validated
@RestController
@RequestMapping("/auth")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado éxitosamente.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"firstName\":\"Matias\",\"lastName\":\"Lopez\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlItIi\" }"))),
            @ApiResponse(responseCode = "400", description = "Error en el ingreso de los campos.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"Errores de Validación\":{\"email\":\"El email debe tener un formato válido.\"},"
                            + "\"fechaHoraError\":\"2024-11-27T10:11:54.251790800\","
                            + "\"estado\":400}")))
    })
    @PostMapping("/register")
    public ShowCreatedUserDTO registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return ResponseEntity.ok(authServiceImplementation.createUser(createUserDTO)).getBody();
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Login de User en el Sistema
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Ingresar al Sistema", description = "Endpoint para ingresar al sistema. " +
            "El endpoint permite al usuario loguearse en el sistema, validando las credenciales, y le brinda el token " +
            "de autorizacion para poder ingresar." +
            "\n\nConsideraciones:" +
            "\n\n- El usuario debe estar previamente registrado y las credenciales deben ser válidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingreso éxitoso.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"firstName\":\"Matias\",\"lastName\":\"Lopez\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlItIi\" }"))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"fechaHoraError\":\"2024-11-27T10:19:40.583225100\","
                            + "\"nombreError\":\"UNAUTHORIZED\","
                            + "\"mensaje\":\"ERROR! Credenciales invalidas!\","
                            + "\"estado\":401}")))
    })
    @PostMapping("/login")
    public ShowCreatedUserDTO login(@RequestBody LoginUserDTO loginUserDTO) {
        return ResponseEntity.ok(authServiceImplementation.login(loginUserDTO.getEmail(),
                loginUserDTO.getPassword())).getBody();
    }
    //-----------------------------------------------------------------------------------------------------------
}
