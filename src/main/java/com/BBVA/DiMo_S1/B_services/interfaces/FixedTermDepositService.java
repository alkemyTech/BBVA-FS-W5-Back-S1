package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface FixedTermDepositService {

    //1- Creación de un Plazo Fijo.
    FixedTermDepositDTO createFixedTermDeposit (HttpServletRequest request, CreateFixedTermDepositDTO createFixedTermDepositDTO);

    //2- Calcular Plazo Fijo.
    double calcularPlazoFijo (List<FixedTermDeposit> plazosFijos);
}
