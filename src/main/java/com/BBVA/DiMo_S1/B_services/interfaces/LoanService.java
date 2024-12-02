package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.loanDTO.CreateLoanDTO;
import com.BBVA.DiMo_S1.D_models.Loan;

public interface LoanService {

    //1- Simular un préstamo.
    Loan simulateLoan(CreateLoanDTO createLoanDTO);
}
