package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.*;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "C- Transacciones")
public class TransactionController {

    @Autowired
    private TransactionServiceImplementation transactionServiceImplementation;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TransactionService transactionService;

    //1- Realizar una transaccion de ARS a otra cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Transferir ARS", description = "Endpoint para realizar una transacción en pesos. " +
            "El endpoint permite al usuario autenticado realizar una transacción en pesos a otra cuenta presente en el sietema." +
            "\n\nConsideraciones:" +
            "\n- El usuario debe tener una cuenta en ARS." +
            "\n- El monto a transferir no puede superar al balance de la cuenta ni al límite de transacción." +
            "\n- El monto a transferir debe ser > 0." +
            "\n- El CBU de la cuenta destino debe corresponder a una cuenta: Existente en el sistema, en ARS y no puede ser el CBU de la propia cuenta del usuario."
    )
    @PostMapping("/sendArs")
    public ResponseEntity<TransactionDTO> sendArs(@RequestBody SimpleTransactionDTO simpleTransactionDTO, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.sendMoney(request, simpleTransactionDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Realizar una transaccion de USD a otra cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Transferir USD", description = "Endpoint para realizar una transacción en dólares. " +
            "El endpoint permite al usuario autenticado realizar una transacción en dólares a otra cuenta presente en el sietema." +
            "\n\nConsideraciones:" +
            "\n- El usuario debe tener una cuenta en USD." +
            "\n- El monto a transferir no puede superar el balance de la cuenta ni al límite de transacción." +
            "\n- El monto a transferir debe ser > 0." +
            "\n- El CBU de la cuenta destino debe corresponder a una cuenta: Existente en el sistema, en USD y no puede ser el CBU de la propia cuenta del usuario."
    )
    @PostMapping("/sendUsd")
    public ResponseEntity<TransactionDTO> sendUsd(@RequestBody SimpleTransactionDTO simpleTransactionDTO, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.sendMoney(request, simpleTransactionDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Depositar dinero en una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Depositar dinero en una cuenta", description = "Endpoint para realizar un depósito en una cuenta. " +
            "El endpoint permite al usuario autenticado realizar un depósito en la cuenta especificada." +
            "\n\nConsideraciones:" +
            "\n- El usuario debe tener la cuenta especificada." +
            "\n- El monto a depositar debe ser mayor a 0."
    )
    @PostMapping("/deposit")
    public ResponseEntity<TransactionCompletaDTO> deposit(@RequestBody TransactionDepositDTO transactionDepositDTO, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.deposit(request, transactionDepositDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

    //4- Ver el detalle de una transaccion buscandola por ID.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Ver detalle de una transacción", description = "Endpoint para poder ver detalle de una transacción. " +
            "El endpoint permite al usuario administrador ver el detalle de una transacción buscandola por ID." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador." +
            "\n- El ID de transacción enviado debe coincidir con una transacción perteneciente al usuario autenticado."
    )
    @GetMapping("admin/detail/{idTransaction}")
    public ResponseEntity<TransactionCompletaDTO> obtainTransactionDetail(HttpServletRequest request, @PathVariable Long idTransaction) {
        TransactionCompletaDTO transactionDetalle = transactionServiceImplementation.transactionDetail(request, idTransaction);
        return ResponseEntity.ok(transactionDetalle);
    }
    //-----------------------------------------------------------------------------------------------------------

    //5- Actualizar la descripción de una transaccion.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Actualizar descripción de una transacción", description = "Endpoint para actualizar la descripción de una transacción. " +
            "El endpoint permite al usuario autenticado actualizar la descripción de una transacción buscandola por ID." +
            "\n\nConsideraciones:" +
            "\n- El usuario debe tener transacciones presentes." +
            "\n- El ID de transacción enviado debe coincidir con una transacción perteneciente al usuario autenticado."
    )
    @PatchMapping("/{idTransaction}")
    public ResponseEntity<TransactionDTO> updateTransactionDescription(@PathVariable("idTransaction") Long transactionId,
                                                                       @RequestBody TransactionUpdateDTO transactionUpdateDTO,
                                                                       HttpServletRequest request) {
        return ResponseEntity.ok().body(transactionServiceImplementation.updateTransactionDescription
                (transactionId, transactionUpdateDTO, request));
    }
    //-----------------------------------------------------------------------------------------------------------

    //6- Obtener listado de transacciones para usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener listado de transacciones", description = "Endpoint para obtener listado de transacciones. " +
            "El endpoint permite al usuario autenticado obtener el listado de sus transacciones realizadas." +
            "\n\nConsideraciones:" +
            "\n- El usuario debe tener transacciones presentes."
    )
    @GetMapping("/")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsUser(HttpServletRequest request) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        return ResponseEntity.ok(transactionServiceImplementation.getAllTransactionsFromUser(userSecurityDTO.getId()));
    }
    //-----------------------------------------------------------------------------------------------------------

    //7- Obtener listado de transacciones como usuario autenticado con rol administrador.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener listado de transacciones como administrador", description = "Endpoint para obtener listado de transacciones. " +
            "El endpoint permite al usuario administrador obtener el listado de transacciones realizadas pertenecientes a un  determinado usuario." +
            "\n\nConsideraciones:" +
            "\n- El usuario autenticado debe ser administrador." +
            "\n- El usuario que se busca debe existir en el sistema y debe tener transacciones presentes."
    )
    @GetMapping("/admin/list/{idUser}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsAdmin(HttpServletRequest request, @PathVariable Long idUser) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        String toUpperCaseRole = userSecurityDTO.getRole();
        if (toUpperCaseRole.toUpperCase().equals("ADMIN")) {
            List<TransactionDTO> transactionDTOList = transactionServiceImplementation.getAllTransactionsFromUser(idUser);
            return ResponseEntity.ok(transactionDTOList);
        } else {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }
    }
    //-----------------------------------------------------------------------------------------------------------

    //Endpoint para pagos
    @PostMapping("/payment")
    public ResponseEntity<?> makePayment(@RequestBody TransactionDTO transactionDTO, HttpServletRequest request) {

        // Llamar al servicio para procesar el pago
        var response = transactionServiceImplementation.makePayment(transactionDTO, request);

        // Devolver respuesta exitosa
        System.out.println(response);
        return ResponseEntity.ok(response);

    }

    //Endpoint para pruebas de paginado
    @GetMapping("/admin/transactions")
    public ResponseEntity<Page<TransactionDTO>> getTransactionsByUser(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<TransactionDTO> transactions = transactionService.getTransactionsByUser(userId, page);
        return ResponseEntity.ok(transactions);
    }
}
