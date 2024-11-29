package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.LoanServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.loanDTO.CreateLoanDTO;
import com.BBVA.DiMo_S1.D_models.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("loan")
public class LoanController {

    @Autowired
    LoanServiceImplementation loanServiceImplementation;

    //1- Simular un préstamo.
    //-----------------------------------------------------------------------------------------------------------
    @PostMapping("/simulate")
    public ResponseEntity<Loan> simulateLoan(CreateLoanDTO createLoanDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanServiceImplementation.simulateLoan(createLoanDTO));
    }
    //-----------------------------------------------------------------------------------------------------------

}
