package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionCompletaDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplementation implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public TransactionDTO sendMoney (HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO) {

        //Inicializamos la transaccion vacia.
        Transaction transactionEmisor = Transaction.builder().build();
        Transaction transactionDestino = Transaction.builder().build();

        //Extraemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        //Obtenemos la lista de cuentas del User autenticado
        List <Account> listAccounts = accountRepository.getByIdUser(userSecurityDTO.getId());

        //Validamos en primer lugar que tenga cuentas asociadas.
        if (listAccounts.isEmpty()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
        }

        //Validamos cual es la accion que quiere realizar el User
        Optional <Account> accountEmisora;

        //Si quiere enviar dolares...
        if (request.getRequestURI().contains("transactions/sendUsd")) {

            //Buscamos en la lista la cuenta en USD.
            accountEmisora = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.USD)
                    .findFirst();

            //Si no tiene cuenta lanzamos excepcion...
            if (accountEmisora.isEmpty()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);

            } else {

                //Si tiene cuenta, hay q ver si la transaccion se puede realizar...
                if (simpleTransactionDTO.getAmount() > accountEmisora.get().getBalance() ||
                        simpleTransactionDTO.getAmount() > accountEmisora.get().getTransactionLimit()) {
                    throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
                }
            }
        } else  {

            //Si quiere enviar pesos..
            //Buscamos en la lista la cuenta en ARS.
            accountEmisora = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.ARS)
                    .findFirst();

            //Si no tiene cuenta lanzamos excepcion...
            if (accountEmisora.isEmpty()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);

            } else {

                //Si tiene cuenta, hay q ver si la transaccion se puede realizar...
                if (simpleTransactionDTO.getAmount() > accountEmisora.get().getBalance() ||
                        simpleTransactionDTO.getAmount() > accountEmisora.get().getTransactionLimit()) {
                    throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
                }
            }
        }

        //Si la cuenta esta en condiciones...

        //Obtenemos la cuenta destino. Verificamos 2 cosas: Que el CBU no sea el del usuario autenticado
        //y que exista.
        Optional <Account> accountBuscada = accountRepository.findByCbu(simpleTransactionDTO.getCBU());

        //Si la cuenta buscada existe...
        if (accountBuscada.isPresent()) {

            //La asignamos a una cuenta...
            Account accountDestino = accountBuscada.get();

            //Verificamos que el id del User de la cuenta sea diferente al del usuario autenticado.
            if (accountDestino.getUser().getId() != userSecurityDTO.getId()) {

                //Verificamos el tipo de cuenta a la cual se desea enviar el dinero..
                if (accountEmisora.get().getCurrency().equals(accountDestino.getCurrency())) {

                    //Realizamos transferencia...

                    //Le creamos un income a la cuenta destino:
                    double balanceConIncome = accountDestino.getBalance() + simpleTransactionDTO.getAmount();
                    accountDestino.setBalance(balanceConIncome);

                    //Le creamos un payment a la cuenta emisora:
                    double balanceConPayment = accountEmisora.get().getBalance() - simpleTransactionDTO.getAmount();
                    accountEmisora.get().setBalance(balanceConPayment);

                    //Actualizamos las cuentas en la BD.
                    accountRepository.save(accountDestino);
                    accountRepository.save(accountEmisora.get());

                    //Seteamos los datos de la transaccion
                    transactionEmisor.setDescription(simpleTransactionDTO.getDescription());
                    transactionEmisor.setAmount(simpleTransactionDTO.getAmount());
                    transactionEmisor.setType(TransactionType.payment);
                    transactionEmisor.setAccount(accountEmisora.get());

                    transactionDestino.setDescription(simpleTransactionDTO.getDescription());
                    transactionDestino.setAmount(simpleTransactionDTO.getAmount());
                    transactionDestino.setType(TransactionType.deposit);
                    transactionDestino.setAccount(accountDestino);

                } else {

                    throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.ACCOUNT_NO_VALIDA);
                }

            } else  {

                throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.ERROR_CUENTA_PROPIA);
            }
        } else {

            throw new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ACCOUNT_NO_ENCONTRADA);
        }

        //Si sale bien la transaccion, la guardamos en la BD.
        Transaction transactionGuardada = transactionRepository.save(transactionEmisor);
        transactionRepository.save(transactionDestino);

        TransactionDTO transactionDTO = new TransactionDTO(transactionGuardada);

        return transactionDTO;
    }


    //
    @Override
    public Map<String, Object> makePayment(TransactionDTO transactionDTO, String token) {
        // Validar que el monto sea positivo
        if (transactionDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }

        // Extraer el usuario autenticado a partir del token
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Usuario no encontrado."));

        // Buscar la cuenta del usuario (considerando tipo de cuenta del DTO)
        Account account = accountRepository.findByUserIdAndCurrency(user.getId(), transactionDTO.getType().ordinal())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una cuenta del tipo indicado para este usuario."));

        // Verificar que haya suficiente saldo
        if (account.getBalance() < transactionDTO.getAmount()) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el pago.");
        }

        // Registrar la transacción como payment
        Transaction payment = Transaction.builder()
                .amount(transactionDTO.getAmount())
                .type(TransactionType.payment)
                .description(transactionDTO.getDescription())
                .transactionDate(LocalDateTime.now())
                .account(account)
                .build();

        transactionRepository.save(payment);

        // Actualizar el balance de la cuenta
        account.setBalance(account.getBalance() - transactionDTO.getAmount());
        accountRepository.save(account);

        // Preparar la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("transaction", new TransactionDTO(payment));
        response.put("updatedAccount", account);

        return response;



    @Override
    public TransactionCompletaDTO deposit(HttpServletRequest request, TransactionDepositDTO transactionDepositDTO){

        //Constructior del deposito
        Transaction deposito = Transaction.builder().build();

        //Extraemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        Optional<Account> cuenta = null;

        List<Account> listAccounts = accountRepository.getByIdUser(userSecurityDTO.getId());


        if(transactionDepositDTO.getCurrencyType().equals(CurrencyType.USD)){
            cuenta = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.USD)
                    .findFirst();
            if(cuenta.isEmpty()){
                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ACCOUNT_NO_ENCONTRADA);
            }
        }else {
            cuenta = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.ARS)
                    .findFirst();
        }


            if (transactionDepositDTO.getAmount() <= 0 ) {
                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_BALANCE_NEGATIVO);
            } else {

                double nuevoBalance = cuenta.get().getBalance() + transactionDepositDTO.getAmount();
                if (nuevoBalance > cuenta.get().getTransactionLimit()){
                    throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_BALANCE_MAYOR_A_DEPOSITO);
                }
                cuenta.get().setBalance(nuevoBalance);
                accountRepository.save(cuenta.get());

                deposito.setAmount(transactionDepositDTO.getAmount());
                deposito.setType(TransactionType.deposit);
                deposito.setDescription(transactionDepositDTO.getDescription());
                deposito.setAccount(cuenta.get());

            }


        Transaction transactionGuardada = transactionRepository.save(deposito);


        TransactionCompletaDTO transactionCompletaDTO = new TransactionCompletaDTO(transactionGuardada);

        return transactionCompletaDTO;

    }

    public List<TransactionDTO> getAllTransactionsFromUser(Long id){

        List<Transaction> transactionList = transactionRepository.getTransactionsByIdUser(id);
        if (!transactionList.isEmpty()){
            return transactionList.stream()
                    .map(TransactionDTO::new)
                    .collect(Collectors.toList());
        }else{
            throw new CustomException(HttpStatus.CONFLICT,ErrorConstants.ERROR_TRANSACTION_NOT_EXIST);
        }

    }


    @Override
    public TransactionCompletaDTO transactionDetail(HttpServletRequest request, Long idTransaction ){
        //Autenticamos el usuario
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        //Buscamos una nueva transaction
        Optional<Transaction> transaction = transactionRepository.findById(idTransaction);

        String toUpperCaseRole = userSecurityDTO.getRole();

        if (toUpperCaseRole.toUpperCase().equals("ADMIN")){
            if(transaction.isEmpty()){
                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NO_SE_ENCONTRO_ID_TRANSACTION);
            }
            TransactionCompletaDTO transactionMostrar = new TransactionCompletaDTO(transaction.get());
            return transactionMostrar;
        }else{
            throw new CustomException(HttpStatus.CONFLICT,"Acceso denegado no es usuario administrador");
        }

    }
}
