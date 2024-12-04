package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.ShowSimulatedFixedTermDeposit;
import jakarta.servlet.http.HttpServletRequest;

public interface FixedTermDepositService {

    //1- Creación de un Plazo Fijo.
    ShowSimulatedFixedTermDeposit createFixedTermDeposit(HttpServletRequest request, CreateFixedTermDepositDTO createFixedTermDepositDTO);

    //2- Liquidar Plazos Fijos.
    void liquidarPlazosFijos();
}
