package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.FixedTermDepositServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fixedTerm")
@Tag(name = "D- Plazos Fijos")
public class FixedTermDepositController {

    @Autowired
    FixedTermDepositServiceImplementation fixedTermDepositServiceImplementation;

    //1- Creación de un Plazo Fijo.
    //-----------------------------------------------------------------------------------------------------
    @Operation(summary = "Creación de un Plazo Fijo", description = "Endpoint para crear un Plazo Fijo. " +
            "El endpoint permite al usuario autenticado crear un Plazo Fijo." +
            "\n\nConsideraciones:" +
            "\n- El plazo fijo solo se puede crear mediante una cuenta en ARS." +
            "\n- El plazo fijo puede ser de 30, 60 o 90 días." +
            "\n- El interés del mismo es del 0.2%" +
            "\n- Lo mínimo a invertir es de $1000." +
            "\n- El monto a invertir debe ser menor al balance de la cuenta." +
            "\n- Para la liquidación del mismo, deben espetrar: 1 minuto (30 días), 2 minutos (60 días) y 3 minutos (90 días)"
    )
    @PostMapping
    public ResponseEntity<FixedTermDepositDTO> createAccount(HttpServletRequest request, @Valid @RequestBody CreateFixedTermDepositDTO
            createFixedTermDepositDTO) {

            FixedTermDepositDTO fixedTermDepositDTO = fixedTermDepositServiceImplementation.
                    createFixedTermDeposit(request, createFixedTermDepositDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermDepositDTO);
    }

    @PostMapping("/simulate")
    public ResponseEntity<FixedTermDepositDTO> simulate(HttpServletRequest request, @Valid @RequestBody CreateFixedTermDepositDTO
            createFixedTermDepositDTO) {

        FixedTermDepositDTO fixedTermDepositDTO = fixedTermDepositServiceImplementation.
                createFixedTermDeposit(request, createFixedTermDepositDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermDepositServiceImplementation.
                createFixedTermDeposit(request, createFixedTermDepositDTO));
    }
    //-----------------------------------------------------------------------------------------------------
}
