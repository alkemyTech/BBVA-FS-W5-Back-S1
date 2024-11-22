package com.BBVA.DiMo_S1.A_controllers;


import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    AccountServiceImplementation accountServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- softDelete de un User de la BD.
    @DeleteMapping("/{idAccount}")
    public ResponseEntity<String> softDelete (@Valid @PathVariable @NotNull
            (message = "El ID de la Account no puede ser nulo.") long idAccount) throws CustomException {

        accountServiceImplementation.softDelete(idAccount);

        return ResponseEntity.ok().body("Account con ID = " + idAccount + " eliminado con éxito!");
    }

    //2- createAccount en la BD
    @PostMapping("/")
    public ResponseEntity<AccountDTO> createAccount(HttpServletRequest request, @Valid @RequestBody String currencyType) {

        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        AccountDTO accountCreada = accountServiceImplementation.createAccount(userSecurityDTO.getId(),
                CurrencyType.valueOf(currencyType.replaceAll("\"", "")));

        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreada);
    }
}
