package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.*;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@RequestMapping("accounts")
@Tag(name = "B- Cuentas")
public class AccountController {

    @Autowired
    AccountServiceImplementation accountServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- Obtener las cuentas pertenecientes al usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener cuentas creadas", description = "Endpoint para obtener las cuentas creadas. " +
            "El endpoint permite al usuario autenticado obtener sus cuentas presentes en el sistema." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe tener cuentas presentes en el sistema."
    )
    @GetMapping("/")
    public ResponseEntity<List<AccountPageDTO>> getAccounts(HttpServletRequest request) {
        return ResponseEntity.ok(accountServiceImplementation.getAccounts(request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Creacion de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Creación de una cuenta", description = "Endpoint para crear una Cuenta. " +
            "El endpoint permite al usuario autenticado crear una cuenta en ARS o USD." +
            "\n\nConsideraciones:" +
            "\n- El tipo de cuenta debe ser ARS o USD. Si se ingresa otro tipo, se lanza excepción." +
            "\n- El límite máximo de cuentas es de 2. Una en ARS y otra en USD.")
    @PostMapping("/")
    public ResponseEntity<ShowCreatedAccountDTO> createAccount(HttpServletRequest request, @Valid @RequestBody CreateAccountDTO
            createAccountDTO) {

        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(accountServiceImplementation.
                createAccount(userSecurityDTO.getId(), createAccountDTO.getTipoDeCuenta()));
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Actualizacion del limite de transaccion de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Actualización de una cuenta", description = "Endpoint para actualizar el límite de " +
            "transacción de una Cuenta. " + "El endpoint permite al usuario autenticado actualizar el limite de " +
            "transacción de su cuenta buscandola por CBU." +
            "\n\nConsideraciones:" +
            "\n- El límite de transacción debe ser de 1000 para arriba. De no ser así, se lanza excepción.." +
            "\n- El CBU debe ser válido. Si el mismo no corresponde a una cuenta del usuario, se lanza excepción.")
    @PatchMapping("/{cbu}")
    public ResponseEntity<ShowUpdateAccountDTO> updateAccount(HttpServletRequest request, @RequestBody
    UpdateAccountDTO updateAccountDTO, @PathVariable String cbu) {

        return ResponseEntity.ok().body(accountServiceImplementation.updateAccount(request, updateAccountDTO, cbu));
    }
    //-----------------------------------------------------------------------------------------------------------

    //4- Obtener el balance de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener balance de cuenta", description = "Endpoint para obtener el balance de una Cuenta. " +
            "El endpoint permite al usuario autenticado obtener el balance de su/sus cuenta/s junto con la lista de " +
            "transacciones y de plazos fijos.")
    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> obtainBalance(HttpServletRequest request) {

        return ResponseEntity.ok().body(accountServiceImplementation.obtainBalance(request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //5- Mostrar las cuentas pertenecientes a un determinado usuario buscandolo por id.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener cuentas creadas", description = "Endpoint para obtener las cuentas creadas. " +
            "El endpoint permite a un administrador obtener las cuentas creadas pertenecientes a un determinado usuario buscandolo por ID" +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador." +
            "\n- El usuario buscado debe existir en el sistema." +
            "\n- El usuario buscado debe tener cuentas presentes en el sistema."
    )
    @GetMapping("/admin/{idUser}")
    public ResponseEntity<List<AccountPageDTO>> getAccountsAdmin(HttpServletRequest request, @PathVariable Long idUser) {
        return ResponseEntity.ok(accountServiceImplementation.getAccountsAdmin(request, idUser));
    }
    //-----------------------------------------------------------------------------------------------------------

    //6- Paginado de cuentas.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener cuentas creadas", description = "Endpoint para obtener las cuentas creadas de los usuarios del sistema. " +
            "El endpoint permite a un administrador obtener las cuentas presentes en el sistema." +
            "\n\nConsideraciones:" +
            "\n\n- El paginado de cuentas esta disponible solamente para usuarios administradores. En caso de " +
            "que se desee realizar la operación como usuario sin permisos de administrador, se lanzará excepción."
    )
    @GetMapping("/admin/")
    public ResponseEntity<Page<AccountPageDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       HttpServletRequest request) {
        return ResponseEntity.ok(accountServiceImplementation.getAll(PageRequest.of(page, size), request));
    }
    //-----------------------------------------------------------------------------------------------------------
}
