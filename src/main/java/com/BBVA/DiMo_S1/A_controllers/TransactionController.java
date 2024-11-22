package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImplementation transactionServiceImplementation;

    @PostMapping("/sendArs")
    public ResponseEntity<?> sendMoney(@RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token) {
        try {
            transactionServiceImplementation.sendArs(transactionDTO, token);
            return ResponseEntity.ok("Transferencia realizada con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<TransactionDTO> sendUsd(@RequestBody SimpleTransactionDTO simpleTransactionDTO, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionServiceImplementation.sendUsd(request, simpleTransactionDTO));
    }
}
