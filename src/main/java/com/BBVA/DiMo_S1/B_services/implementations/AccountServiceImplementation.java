package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.AccountService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.*;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

    //1- Mostrar las cuentas pertenecientes al usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public List<AccountPageDTO> getAccounts(HttpServletRequest request) {

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Obtenemos la lista de cuentas pertenecientes al usuario.
        List<Account> accountList = accountRepository.getByIdUser(userSecurityDTO.getId());

        if (accountList.isEmpty()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.USER_SIN_CUENTAS);
        }

        return accountList.stream().map(AccountPageDTO::new).collect(Collectors.toList());
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Creacion de una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public ShowCreatedAccountDTO createAccount(final long idUser, final CurrencyType currencyType) {
        ShowCreatedAccountDTO accountDTO;

        //Obtenemos la lista de cuentas del usuario.
        List<Account> listaCuentas = accountRepository.getByIdUser(idUser);

        //Si ya tiene una cuenta en dolares y otra en pesos...
        if (listaCuentas.size() == 2) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.LIMITE_DE_CUENTAS_ALCANZADO);
        }

        //Boolean para determinar si existe cuenta en pesos.
        boolean existeCuentaEnPesos = listaCuentas.size() == 1 && listaCuentas.get(0).
                getCurrency().equals(CurrencyType.ARS);

        //Si no tiene cuentas o si solo tiene cuenta en pesos y quiere crear una en dolares...
        if (listaCuentas.isEmpty() || (existeCuentaEnPesos && !currencyType.equals(CurrencyType.ARS))) {
            //Inicializamos una cuenta.
            Account account = Account.builder().build();
            //Traemos el usario de la BD.
            User user = userRepository.getById(idUser);

            //Seteamos los datos del usuario.
            account.setUser(user);
            account.setBalance(0);
            account.setCbu(generateCBU());
            account.setCurrency(currencyType);
            if (currencyType.equals(CurrencyType.ARS)) {

                account.setTransactionLimit(300000);
            } else {

                account.setTransactionLimit(1000);
            }

            //Obtenemos la cuenta guardada en la BD.
            Account accountGuardada = accountRepository.save(account);

            //Seteamos el DTO con los datos de la cuenta guardada.
            accountDTO = new ShowCreatedAccountDTO(accountGuardada);

        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_CUENTA_EN_PESOS_YA_EXISTENTE);
        }

        return accountDTO;
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Actualizar el limite de transacción de una Cuenta.
    //------------------------------------------------------------------------------------------------------------
    @Override
    public ShowUpdateAccountDTO updateAccount(HttpServletRequest request, UpdateAccountDTO updateAccountDTO,
                                              String cbu) {

        //Verificamos que el limite ingresado sea válido.
        if (updateAccountDTO.getTransactionLimit() <= 1000) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.LIMITE_TRANSACCION_INVALIDO);
        }

        ShowUpdateAccountDTO showUpdateAccountDTO;

        //Obtenemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Buscamos la Account por id.
        Optional<Account> account = accountRepository.findByCbu(cbu);

        //Si la cuenta existe, no nos alcanza. Debemos verificar si la misma pertenece al User autenticado.
        if (account.isPresent() && account.get().getUser().getId() == userSecurityDTO.getId()) {
            //Modificamos el transactionLimit.
            account.get().setTransactionLimit(updateAccountDTO.getTransactionLimit());

            //Seteamos fecha de actualización.
            account.get().setUpdateDate(LocalDateTime.now());

            //Guardamos la Account nuevamente en la BD.
            accountRepository.save(account.get());

            //Seteamos los datos de la account en el DTO.
            showUpdateAccountDTO = new ShowUpdateAccountDTO(account.get());
        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.CBU_INVALIDO);
        }

        return showUpdateAccountDTO;
    }
    //------------------------------------------------------------------------------------------------------------------

    //4- Obtener balance de una Cuenta
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public BalanceDto obtainBalance(HttpServletRequest request) {

        //Creamos un balanceDTO y lo inicializamos vacío.
        BalanceDto balanceDto = BalanceDto.builder().build();

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Obtenemos la lista de cuentas del User.
        List<Account> listAccounts = accountRepository.getByIdUser(userSecurityDTO.getId());

        //Creamos una lista de Transactions y de FixedTermDeposits.
        List<Transaction> transactionList;
        List<FixedTermDeposit> fixedTermDeposits;

        //En caso de que la persona no tenga ninguna cuenta...
        if (listAccounts.isEmpty()) {

            throw new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ERROR_BALANCE);

        } else {

            //Seteamos el balance dependiendo del tipo de cuenta que se trate.
            for (Account account : listAccounts) {
                if (account.getCurrency().equals(CurrencyType.ARS)) {
                    balanceDto.setBalanceArs(account.getBalance());
                } else {
                    balanceDto.setBalanceUsd(account.getBalance());
                }
            }

            //Traemos la lista de Transactions y de FixedTermDeposits del User.
            Pageable pageable = Pageable.unpaged();
            transactionList = transactionRepository.getTransactionsByIdUserPageable(userSecurityDTO.getId(), pageable).getContent();
            fixedTermDeposits = fixedTermDepositRepository.getFixedTermDepositByIdUser(userSecurityDTO.getId());

            List<TransactionDTO> transactionDTOList = transactionList.stream()
                    .map(TransactionDTO::new)
                    .collect(Collectors.toList());

            List<FixedTermDepositDTO> fixedTermDepositDTOList = fixedTermDeposits.stream()
                    .map(FixedTermDepositDTO::new)
                    .collect(Collectors.toList());

            balanceDto.setFixedTermDeposits(fixedTermDepositDTOList);
            balanceDto.setTransactions(transactionDTOList);
        }

        return balanceDto;
    }
    //------------------------------------------------------------------------------------------------------------------

    //5- Mostrar las cuentas pertenecientes a un determinado usuario buscandolo por id.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public List<AccountPageDTO> getAccountsAdmin(HttpServletRequest request, Long idUser) {

        //Obtenemos el usuario autenticado y el rol.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Validamos que el usuario autenticado sea administrador.
        if (!userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }

        //Buscamos al usuario que se quiere actualizar y lanzamos excepción si no existe.
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO));

        List<Account> accountList = accountRepository.getByIdUser(user.getId());

        if (accountList.isEmpty()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.USER_SIN_CUENTAS);
        }

        return accountList.stream().map(AccountPageDTO::new).collect(Collectors.toList());
    }
    //-----------------------------------------------------------------------------------------------------------

    //6- Paginado de cuentas
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public Page<AccountPageDTO> getAll(Pageable pageable, HttpServletRequest request) {

        //Obtenemos el User logueado y el Role.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        String toUpperCaseRole = userSecurityDTO.getRole();

        if (toUpperCaseRole.toUpperCase().equals("ADMIN")) {

            return accountRepository.findAll(pageable)
                    .map(AccountPageDTO::new);
        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.SIN_PERMISO);
        }
    }
    //-----------------------------------------------------------------------------------------------------------

    @Override
    public AccountDTO obetenerPorCbu(String cbu) {

        Account cuentaBuscada = accountRepository.findByCbuAndSoftDeleteIsNull(cbu)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ACCOUNT_NO_ENCONTRADA));

        return new AccountDTO(cuentaBuscada);
    }

    //-----------------------------------------------------------------------------------------------------------

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