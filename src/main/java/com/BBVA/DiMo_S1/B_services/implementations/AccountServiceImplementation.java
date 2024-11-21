package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.AccountService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.CreateAccountDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class AccountServiceImplementation implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImplementation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //1- softDelete de un User de la BD.
    @Override
    public void softDelete(long idAccount) throws CustomException {

        //Buscamos la Account por ID. En caso de que no exista, lanzamos excepción.
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ACCOUNT_NO_ENCONTRADA));

        //En caso de que la Account exista, no nos alzanza. Debemos verificar si el campo ("soft_delete" == null).
        if (account.getSoftDelete() == null) {
            account.setSoftDelete(LocalDateTime.now());
            accountRepository.save(account);
        } else {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.DELETE_NO_VALIDO_ACCOUNT);
        }
    }


    @Override
    public Account createAccount(CreateAccountDTO createAccountDTO) throws CustomException {

        Account account = Account.builder().build();

        return account;

    }

}
