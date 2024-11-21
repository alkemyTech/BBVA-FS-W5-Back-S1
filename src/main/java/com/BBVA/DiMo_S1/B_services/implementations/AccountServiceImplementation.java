package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.AccountService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;

import com.BBVA.DiMo_S1.D_models.Account;

import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.time.LocalDateTime;

@Service

public class AccountServiceImplementation implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
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
    public AccountDTO createAccount( final Long idUsuario, CurrencyType currencyType){

        if(accountRepository.getByIdUser())

        Account account = Account.builder().build();

        User user = userRepository.getById(idUsuario);

        account.setBalance(0);

        account.setCbu(generateCBU());

        account.setCurrency(currencyType);

        if(currencyType.equals(CurrencyType.ARS)){
            account.setTransactionLimit(300000);
        } else {
            account.setTransactionLimit(1000);
        }

        AccountDTO accountDTO = new AccountDTO(account);



    }





    @Override
    public String generateCBU() {
        Random rand = new Random();
        // Generar código de banco de 3 dígitos (por ejemplo, Banco Nación: 001)
        String banco = String.format("%03d", rand.nextInt(1000));
        // Generar sucursal de 4 dígitos (por ejemplo, 0001)
        String sucursal = String.format("%04d", rand.nextInt(10000));
        // Generar número de cuenta de 13 dígitos
        String cuenta = String.format("%013d", rand.nextLong(10000000000000L));
        // Concatenar banco, sucursal y cuenta para calcular el CBU base
        String cbuBase = banco + sucursal + cuenta;

        // Retornar el CBU completo (22 dígitos)
        return cbuBase;
    }



}
