package com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateFixedTermDepositDTO {

    private double amount;
    private int cantidadDias;
}
