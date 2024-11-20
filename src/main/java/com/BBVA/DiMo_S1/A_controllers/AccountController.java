package com.BBVA.DiMo_S1.A_controllers;


import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImpl;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
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
    AccountServiceImpl accountServiceImpl;

    //1- softDelete de un User de la BD.
    @DeleteMapping("/{idAccount}")
    public ResponseEntity<String> softDelete (@Valid @PathVariable @NotNull
            (message = "El ID de la Account no puede ser nulo.") long idAccount) throws CustomException {

        accountServiceImpl.softDelete(idAccount);

        return ResponseEntity.ok().body("Account con ID = " + idAccount + " eliminado con éxito!");
    }

    //2- createAccount en la BD
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account){
        Account accountCreada = accountServiceImpl.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreada);
    }


}
