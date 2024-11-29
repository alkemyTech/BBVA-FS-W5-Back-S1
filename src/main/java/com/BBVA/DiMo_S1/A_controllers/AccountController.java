package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.*;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gestión de Cuentas", description = "account-controller")
@Validated
@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    AccountServiceImplementation accountServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- Creacion de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Creación de una Cuenta", description = "Endpoint para crear una Cuenta. " +
            "El endpoint permite al usuario autenticado crear una cuenta en ARS o USD." +
            "\n\nConsideraciones:" +
            "\n- El tipo de cuenta debe ser ARS o USD. Si se ingresa otro tipo, se lanza excepción." +
            "\n- El límite máximo de cuentas es de 2. Una en ARS y otra en USD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada con éxito.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"cbu\":\"98821905335515405923\",\"balance\":\"0\",\"currency\":\"USD\"," +
                            "\"transactionLimit\":\"5000\"}"))),
            @ApiResponse(responseCode = "409", description = "Límite de cuentas alcanzado.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"fechaHoraError\":\"2024-11-27T17:51:29.421969700\","
                            + "\"nombreError\":\"CONFLICT\","
                            + "\"mensaje\":\"ERROR! Ya alcanzaste el límite máximo de cuentas.\","
                            + "\"estado\":409}")))
    })
    @PostMapping("/")
    public ResponseEntity<ShowCreatedAccountDTO> createAccount(HttpServletRequest request, @Valid @RequestBody CreateAccountDTO
            createAccountDTO) {

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(accountServiceImplementation.
                createAccount(userSecurityDTO.getId(), createAccountDTO.getTipoDeCuenta()));
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Actualizacion del limite de transaccion de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Actualización de una Cuenta", description = "Endpoint para actualizar el límite de " +
            "transacción de una Cuenta. " + "El endpoint permite al usuario autenticado actualizar el limite de " +
            "transacción de su cuenta buscandola por CBU." +
            "\n\nConsideraciones:" +
            "\n- El límite de transacción debe ser de 1000 para arriba. De no ser así, se lanza excepción.." +
            "\n- El CBU debe ser válido. Si el mismo no corresponde a una cuenta del usuario, se lanza excepción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada con éxito.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"cbu\":\"98821905335515405923\",\"currency\":\"ARS\",\"transactionLimit\":\"500000\"," +
                            "\"balance\":\"0\",\"creationDate\":\"2024-11-27T17:51:29.421969700\",\"updateDate\":\"2024-11-27T17:51:29.421969700\"}"))),
            @ApiResponse(responseCode = "409", description = "Límite de transacción no válido.", content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"fechaHoraError\":\"2024-11-27T17:51:29.421969700\","
                            + "\"nombreError\":\"CONFLICT\","
                            + "\"mensaje\":\"ERROR! El limite de transacción ingresado no es válido. Debe ser si o si mayor o igual a 1000.\","
                            + "\"estado\":409}"))),
    })
    @PatchMapping("/{cbu}")
    public ResponseEntity<ShowUpdateAccountDTO> updateAccount(HttpServletRequest request, @RequestBody
    UpdateAccountDTO updateAccountDTO, @PathVariable String cbu) {

        return ResponseEntity.ok().body(accountServiceImplementation.updateAccount(request, updateAccountDTO, cbu));
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Obtener el balance de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> obtainBalance(HttpServletRequest request) {

        return ResponseEntity.ok().body(accountServiceImplementation.obtainBalance(request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //4- softDelete de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{idAccount}")
    public ResponseEntity<String> softDelete(final HttpServletRequest request, @Valid @PathVariable @NotNull
            (message = "El ID de la Account no puede ser nulo.") long idAccount) throws CustomException {

        accountServiceImplementation.softDelete(request, idAccount);

        return ResponseEntity.ok().body("Account con ID = " + idAccount + " eliminado con éxito!");
    }
    //-----------------------------------------------------------------------------------------------------------
}
