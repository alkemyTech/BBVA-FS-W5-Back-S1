package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.LoanServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.loanDTO.CreateLoanDTO;
import com.BBVA.DiMo_S1.D_models.Loan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@Tag(name = "E- Préstamos")
public class LoanController {

    @Autowired
    LoanServiceImplementation loanServiceImplementation;

    //1- Simular un préstamo.
    //-----------------------------------------------------------------------------------------------------------
    @Operation(summary = "Simulación de un Préstamo", description = "Endpoint para simular un Préstamo. " +
            "El endpoint permite al usuario autenticado simular un préstamo." +
            "\n\nConsideraciones:" +
            "\n- El préstamo puede ser de 12, 24 o 36 meses." +
            "\n- El interés es del 0.5%" +
            "\n- El monto a invertir debe estar entre $10000 y $10000000"
    )
    @PostMapping("/simulate")
    public ResponseEntity<Loan> simulateLoan(@RequestBody CreateLoanDTO createLoanDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanServiceImplementation.simulateLoan(createLoanDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

}
