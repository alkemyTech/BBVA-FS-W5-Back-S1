package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImplementation transactionServiceImplementation;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/sendArs")
    public ResponseEntity<TransactionDTO> sendArs(@RequestBody SimpleTransactionDTO simpleTransactionDTO, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.sendMoney(request, simpleTransactionDTO));
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<TransactionDTO> sendUsd(@RequestBody SimpleTransactionDTO simpleTransactionDTO, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.sendMoney(request, simpleTransactionDTO));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionCompletaDTO> deposit(@RequestBody TransactionDepositDTO transactionDepositDTO, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.deposit(request, transactionDepositDTO));
    }

    @GetMapping()
    public ResponseEntity<Page<TransactionDTO>> getAllTransactionsUser(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page
    ) {
        // Validar el token y obtener información del usuario loggeado
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        // Llamar al servicio para obtener las transacciones del usuario loggeado
        Page<TransactionDTO> transactionPage = transactionService.getAllTransactionsFromUser(userSecurityDTO.getId(), page);

        return ResponseEntity.ok(transactionPage);
    }

    @GetMapping("/admin/{idUser}")
    public ResponseEntity<Page<TransactionDTO>> getAllTransactionsAdmin(
            HttpServletRequest request,
            @PathVariable Long idUser,
            @RequestParam(defaultValue = "0") int page
    ) {
        // Validar si el usuario tiene rol de administrador
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        if (!"ADMIN".equalsIgnoreCase(userSecurityDTO.getRole())) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.ERROR_NOT_ADMIN);
        }

        // Llamar al servicio para obtener las transacciones del usuario especificado
        Page<TransactionDTO> transactionPage = transactionService.getAllTransactionsFromUser(idUser, page);

        return ResponseEntity.ok(transactionPage);
    }

    @GetMapping("/{idTransaction}")
    public ResponseEntity<TransactionCompletaDTO> obtainTransactionDetail(HttpServletRequest request, @PathVariable Long idTransaction){
        TransactionCompletaDTO transactionDetalle = transactionServiceImplementation.transactionDetail(request,idTransaction);
        return ResponseEntity.ok(transactionDetalle);
    }

    //Endpoint para pagos
    @PostMapping("/payment")
    public ResponseEntity<?> makePayment(@RequestBody TransactionDepositDTO transactionDTO, HttpServletRequest request){

        // Llamar al servicio para procesar el pago
        var response = transactionServiceImplementation.makePayment(transactionDTO, request);

        // Devolver respuesta exitosa
        System.out.println(response);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTransactionDescription(
            @PathVariable("id") Long transactionId,
            @RequestBody TransactionUpdateDTO transactionUpdateDTO,
            HttpServletRequest request) {

        try {
            // Obtén la descripción desde el DTO
            String newDescription = transactionUpdateDTO.getDescription();

            // Llama al servicio para actualizar la descripción de la transacción
            TransactionDTO updatedTransaction = transactionService.updateTransactionDescription(transactionId, newDescription, request);

            // Devuelve la transacción actualizada
            return ResponseEntity.ok(updatedTransaction);
        } catch (IllegalArgumentException e) {
            // Maneja errores específicos con una respuesta adecuada
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Manejo genérico de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }

}
