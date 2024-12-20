package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.FixedTermDepositServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositResultsDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.ShowSimulatedFixedTermDeposit;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixedTerm")
@Tag(name = "D- Plazos Fijos")
public class FixedTermDepositController {

    @Autowired
    FixedTermDepositServiceImplementation fixedTermDepositServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- Creación de un Plazo Fijo.
    //-----------------------------------------------------------------------------------------------------
    @Operation(summary = "Creación de un Plazo Fijo", description = "Endpoint para crear un Plazo Fijo. " +
            "El endpoint permite al usuario autenticado crear un Plazo Fijo." +
            "\n\nConsideraciones:" +
            "\n- El plazo fijo solo se puede crear mediante una cuenta en ARS." +
            "\n- El plazo fijo puede ser de 30, 60 o 90 días." +
            "\n- El interés del mismo es del 2%." +
            "\n- Lo mínimo a invertir es de $1000." +
            "\n- El monto a invertir debe ser menor al balance de la cuenta." +
            "\n- Para la liquidación del mismo, deben espetrar: 1 minuto (30 días), 2 minutos (60 días) y 3 minutos (90 días)"
    )
    @PostMapping
    public ResponseEntity<ShowSimulatedFixedTermDeposit> createFixedTermDeposit(HttpServletRequest request, @Valid @RequestBody
    CreateFixedTermDepositDTO createFixedTermDepositDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermDepositServiceImplementation.
                createFixedTermDeposit(request, createFixedTermDepositDTO));
    }
    //-----------------------------------------------------------------------------------------------------

    //2- Simulación de un Plazo Fijo.
    //-----------------------------------------------------------------------------------------------------
    @Operation(summary = "Simulación de un Plazo Fijo", description = "Endpoint para simular un Plazo Fijo. " +
            "El endpoint permite al usuario autenticado simular un Plazo Fijo." +
            "\n\nConsideraciones:" +
            "\n- El plazo fijo solo se puede crear mediante una cuenta en ARS." +
            "\n- El plazo fijo puede ser de 30, 60 o 90 días." +
            "\n- El interés del mismo es del 2%." +
            "\n- Lo mínimo a invertir es de $1000."
    )
    @PostMapping("/simulate")
    public ResponseEntity<ShowSimulatedFixedTermDeposit> simulateFixedTermDeposit(HttpServletRequest request, @Valid @RequestBody
    CreateFixedTermDepositDTO createFixedTermDepositDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermDepositServiceImplementation.
                createFixedTermDeposit(request, createFixedTermDepositDTO));
    }
    //-----------------------------------------------------------------------------------------------------

    //3- Listar Plazos Fijos pertenecientes al usuario.
    //-----------------------------------------------------------------------------------------------------
    @Operation(summary = "Obtener listado de Plazos Fijos", description = "Endpoint para obtener listado de Plazos Fijos. " +
            "El endpoint permite al usuario autenticado obtener el listado de sus Plazos Fijos creados. Los mismos, pueden " +
            "ser paginadas."
    )
    @GetMapping
    public ResponseEntity<Page<FixedTermDepositDTO>> getAllFixedTermDepositFromUser(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {  // Ajuste aquí
        // Validar el token y obtener información del usuario loggeado
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        // Llamar al servicio para obtener las transacciones del usuario loggeado
        Page<FixedTermDepositDTO> fixedTermDepositPage = fixedTermDepositServiceImplementation.getAllFixedTermDepositFromUser
                (userSecurityDTO.getId(), page, size);  // Ajuste aquí
        return ResponseEntity.ok(fixedTermDepositPage);
    }
    //-----------------------------------------------------------------------------------------------------

    @GetMapping("/totals")
    public ResponseEntity<FixedTermDepositResultsDTO> getFixedTermDepositsInfo(HttpServletRequest request) {

        return ResponseEntity.ok(fixedTermDepositServiceImplementation.getFixedTermDepositsInfo(request));
    }
}
