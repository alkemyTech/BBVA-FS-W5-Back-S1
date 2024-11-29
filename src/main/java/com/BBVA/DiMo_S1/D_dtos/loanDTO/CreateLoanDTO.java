package com.BBVA.DiMo_S1.D_dtos.loanDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateLoanDTO {

    private double monto;
    private int plazo;
}

