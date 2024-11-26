package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionCompletaDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImplementation transactionServiceImplementation;

    @Autowired
    private JwtService jwtService;

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

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsUser(HttpServletRequest request) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));
        return ResponseEntity.ok(transactionServiceImplementation.getAllTransactionsFromUser(userSecurityDTO.getId()));
    }

    @GetMapping("/transactions/{idUser}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsAdmin(HttpServletRequest request, @PathVariable Long idUser) {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));
        String toUpperCaseRole = userSecurityDTO.getRole();
        if (toUpperCaseRole.toUpperCase().equals("ADMIN")) {
            List<TransactionDTO> transactionDTOList = transactionServiceImplementation.getAllTransactionsFromUser(idUser);
            return ResponseEntity.ok(transactionDTOList);

        } else {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }
    }
    @GetMapping("/{idTransaction}")
    public ResponseEntity<TransactionCompletaDTO> obtainTransactionDetail(HttpServletRequest request, @PathVariable Long idTransaction){
        TransactionCompletaDTO transactionDetalle = transactionServiceImplementation.transactionDetail(request,idTransaction);
        return ResponseEntity.ok(transactionDetalle);
    }

    //Endpoint para pagos
    @PostMapping("/payment")
    public ResponseEntity<?> makePayment(@RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token) {
        try {
            // Llamar al servicio para procesar el pago
            var response = transactionServiceImplementation.makePayment(transactionDTO, token);

            // Devolver respuesta exitosa
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }
}
