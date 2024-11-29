package com.BBVA.DiMo_S1.A_controllers;


import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.*;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        AccountDTO accountCreada = accountServiceImplementation.createAccount(userSecurityDTO.getId(),
                CurrencyType.valueOf(currencyType.replaceAll("\"", "")));

        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreada);
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> obtainBalance(HttpServletRequest request){
        
        return ResponseEntity.ok().body(accountServiceImplementation.obtainBalance(request));
    }

    @PatchMapping ("/{cbu}")
    public ResponseEntity<ShowUpdateAccountDTO> updateAccount (HttpServletRequest request, @RequestBody
    UpdateAccountDTO updateAccountDTO, @PathVariable String cbu) {

        return ResponseEntity.ok().body(accountServiceImplementation.updateAccount(request, updateAccountDTO, cbu));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<AccountPageDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) throws CustomException {
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));
        String toUpperCaseRole = userSecurityDTO.getRole();

        if (toUpperCaseRole.toUpperCase().equals("ADMIN")){
            return ResponseEntity.ok(accountServiceImplementation.getAll(PageRequest.of(page, size)));
        }else{
            throw new CustomException(HttpStatus.CONFLICT,ErrorConstants.CREDENCIALES_INVALIDAS);
        }
    }
}
