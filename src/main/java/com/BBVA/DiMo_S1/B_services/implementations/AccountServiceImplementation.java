package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.AccountService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowUpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.UpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplementation implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TransactionServiceImplementation transactionServiceImplementation;

    @Autowired
    private FixedTermDepositServiceImplementation fixedTermDepositServiceImplementation;

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
    public AccountDTO createAccount(final Long idUsuario, CurrencyType currencyType) {

        List<Account> listaCuentas = accountRepository.getByIdUser(idUsuario);

        AccountDTO accountDTO;

        boolean existeCuentaEnPesos = listaCuentas.size() == 1 && listaCuentas.get(0).
                getCurrency().equals(CurrencyType.ARS);

        if (listaCuentas.isEmpty() || (existeCuentaEnPesos && !currencyType.equals(CurrencyType.ARS))) {

            Account account = Account.builder().build();

            User user = userRepository.getById(idUsuario);

            account.setUser(user);

            account.setBalance(0);

            account.setCbu(generateCBU());

            account.setCurrency(currencyType);

            if (currencyType.equals(CurrencyType.ARS)) {

                account.setTransactionLimit(300000);

            } else {

                account.setTransactionLimit(1000);
            }

            Account accountGuardada = accountRepository.save(account);

            accountDTO = new AccountDTO(accountGuardada);

        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.OPERACION_NO_VALIDA);
        }

        return accountDTO;
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada para el email: " + email));
    }

    //Obtener balance de una Account
    @Override
    public BalanceDto obtainBalance(HttpServletRequest request) {

        //Creamos un balanceDTO y lo inicializamos vacío.
        BalanceDto balanceDto = BalanceDto.builder().build();

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        //Obtenemos la lista de cuentas del User.
        List<Account> listAccounts = accountRepository.getByIdUser(userSecurityDTO.getId());

        //Creamos una lista de Transactions y de FixedTermDeposits.
        List<Transaction> transactionList;
        List<FixedTermDeposit> fixedTermDeposits;

        //En caso de que la persona no tenga ninguna cuenta...
        if (listAccounts.isEmpty()) {

            throw new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ERROR_BALANCE);

        } else {

            //Antes de setear el balance de la cuenta, debemos verificar si tiene algun Plazo Fijo que ya
            //venció. En ese caso, hay que actualizar su balance y generar un deposito en su cuenta.
            //----------------------------------------------------------------------------------------------
            //1- Obtenego la lista de Plazos Fijos.
            fixedTermDeposits = fixedTermDepositRepository.getFixedTermDepositByIdUser(userSecurityDTO.getId());

            //2- Mando la lista de FixedTermDeposit por parametro a mi funcion en el servicio del Plazo Fijo.
            double gananciaPlazoFijo = fixedTermDepositServiceImplementation.calcularPlazoFijo(fixedTermDeposits);

            //3- En caso de que haya algun Plazo Fijo ya vencido, es decir, existe algun tipo de ganancia, debemos
            //setearle el nuevo balance a la cuenta del User.
            if (gananciaPlazoFijo != 0) {

                //Obtengo la cuenta en pesos del User.
                Optional<Account> account = accountRepository.getArsAccountByIdUser(userSecurityDTO.getId());

                //Genero una Transaction del tipo Deposit para que el User se de cuenta de que se le depositó el
                //Plazo Fijo en su cuenta.
                TransactionDepositDTO depositoPlazoFijo = TransactionDepositDTO.builder().build();
                depositoPlazoFijo.setAmount(gananciaPlazoFijo);
                depositoPlazoFijo.setDescription("Liquidación de Plazo Fijo");
                depositoPlazoFijo.setCurrencyType(CurrencyType.ARS);
                transactionServiceImplementation.deposit(request, depositoPlazoFijo);
            }
            //----------------------------------------------------------------------------------------------

            //Seteamos el balance dependiendo del tipo de cuenta que se trate.
            for (Account account : listAccounts) {
                if (account.getCurrency().equals(CurrencyType.ARS)) {
                    balanceDto.setBalanceArs(account.getBalance());
                } else {
                    balanceDto.setBalanceUsd(account.getBalance());
                }
            }

            //Traemos la lista de Transactions y de FixedTermDeposits del User.
            transactionList = transactionRepository.getTransactionsByIdUser(userSecurityDTO.getId());


            List<TransactionDTO> transactionDTOList = transactionList.stream()
                    .map(TransactionDTO::new)
                    .collect(Collectors.toList());

            List<FixedTermDepositDTO> fixedTermDepositDTOList = fixedTermDeposits.stream()
                    .map(FixedTermDepositDTO::new)
                    .collect(Collectors.toList());

            balanceDto.setTransactionDTOList(transactionDTOList);
            balanceDto.setFixedTermDepositDTOList(fixedTermDepositDTOList);

        }

        return balanceDto;
    }

    @Override
    public ShowUpdateAccountDTO updateAccount(HttpServletRequest request, UpdateAccountDTO updateAccountDTO, String cbu) {

        //Inicializamos vacio el DTO.
        ShowUpdateAccountDTO showUpdateAccountDTO = ShowUpdateAccountDTO.builder().build();

        //Obtenemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        //Buscamos la Account por id.
        Optional <Account> account = accountRepository.findByCbu(cbu);

        //Si la cuenta existe, no nos alcanza. Debemos verificar si la misma pertenece al User autenticado.
        if (account.isPresent() && account.get().getUser().getId() == userSecurityDTO.getId()) {

            //Modificamos el transactionLimit.
            account.get().setTransactionLimit(updateAccountDTO.getTransactionLimit());

            //Guardamos la Account nuevamente en la BD.
            accountRepository.save(account.get());

            //Seteamos los datos de la account en el DTO.
            showUpdateAccountDTO = new ShowUpdateAccountDTO(account.get());

        } else {

            throw new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ACCOUNT_NO_ENCONTRADA);
        }

        return showUpdateAccountDTO;
    }

    private String generateCBU() {
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