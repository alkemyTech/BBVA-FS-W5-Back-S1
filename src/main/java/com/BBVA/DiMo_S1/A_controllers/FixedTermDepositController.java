package com.BBVA.DiMo_S1.A_controllers;

import com.BBVA.DiMo_S1.B_services.implementations.FixedTermDepositServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fixedTerm")
public class FixedTermDepositController {

    @Autowired
    FixedTermDepositServiceImplementation fixedTermDepositServiceImplementation;

    @PostMapping
    public ResponseEntity<FixedTermDepositDTO> createAccount(HttpServletRequest request, @Valid @RequestBody CreateFixedTermDepositDTO
            createFixedTermDepositDTO) {

            FixedTermDepositDTO fixedTermDepositDTO = fixedTermDepositServiceImplementation.
                    createFixedTermDeposit(request, createFixedTermDepositDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermDepositDTO);
    }

}
