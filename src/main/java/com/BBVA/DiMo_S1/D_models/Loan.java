package com.BBVA.DiMo_S1.D_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Loan {

    private double monto;
    private int plazo;
    private double interes;
    private BigDecimal cuotaMensual;
    private BigDecimal total;
}