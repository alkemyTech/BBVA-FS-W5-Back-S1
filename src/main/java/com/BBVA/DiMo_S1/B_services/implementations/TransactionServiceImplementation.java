package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionUpdateDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    //1- Realizar una transaccion de dinero a otra cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public TransactionDTO sendMoney(HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO) {

        //Inicializamos la transacciones vacias.
        Transaction transactionEmisor = Transaction.builder().build();
        Transaction transactionDestino = Transaction.builder().build();

        //Extraemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Obtenemos la lista de cuentas del User autenticado
        List<Account> listAccounts = accountRepository.getByIdUser(userSecurityDTO.getId());

        //Validamos en primer lugar que tenga cuentas asociadas.
        if (listAccounts.isEmpty()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_SIN_CUENTAS);
        }

        //Validamos cual es la accion que quiere realizar el User
        Optional<Account> accountEmisora;

        //Si quiere enviar dolares...
        if (request.getRequestURI().contains("transactions/sendUsd")) {

            //Buscamos en la lista la cuenta en USD.
            accountEmisora = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.USD)
                    .findFirst();

            //Si no tiene cuenta lanzamos excepcion...
            if (accountEmisora.isEmpty()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_AUSENCIA_CUENTA_DOLARES);

            } else {

                //Si tiene cuenta, hay q ver si la transaccion se puede realizar...
                if (simpleTransactionDTO.getAmount() > accountEmisora.get().getBalance() ||
                        simpleTransactionDTO.getAmount() > accountEmisora.get().getTransactionLimit()) {
                    throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
                }
            }

        } else {

            //Si quiere enviar pesos..
            //Buscamos en la lista la cuenta en ARS.
            accountEmisora = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.ARS)
                    .findFirst();

            //Si no tiene cuenta lanzamos excepcion...
            if (accountEmisora.isEmpty()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_AUSENCIA_CUENTA_PESOS);

            } else {

                //Si tiene cuenta, hay q ver si la transaccion se puede realizar...
                if (simpleTransactionDTO.getAmount() > accountEmisora.get().getBalance() ||
                        simpleTransactionDTO.getAmount() > accountEmisora.get().getTransactionLimit() ||
                        simpleTransactionDTO.getAmount() <= 0) {
                    throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
                }
            }
        }

        //Si la cuenta esta en condiciones...

        //Obtenemos la cuenta destino. Verificamos 2 cosas: Que el CBU no sea el del usuario autenticado
        //y que exista.
        Optional<Account> accountBuscada = accountRepository.findByCbu(simpleTransactionDTO.getCBU());

        //Si la cuenta buscada existe y no fue dada de baja...
        if (accountBuscada.isPresent() && accountBuscada.get().getSoftDelete() == null) {

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
                    transactionEmisor.setAccountDestino(accountDestino);

                    transactionDestino.setDescription(simpleTransactionDTO.getDescription());
                    transactionDestino.setAmount(simpleTransactionDTO.getAmount());
                    transactionDestino.setType(TransactionType.deposit);
                    transactionDestino.setAccount(accountDestino);
                    transactionDestino.setAccountDestino(null);

                } else {

                    throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.ACCOUNT_NO_VALIDA);
                }

            } else {

                throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.ERROR_CUENTA_PROPIA);
            }

        } else {

            throw new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ACCOUNT_NO_ENCONTRADA_O_DADA_DE_BAJA);
        }

        //Si sale bien la transaccion, la guardamos en la BD.
        Transaction transactionGuardada = transactionRepository.save(transactionEmisor);
        transactionRepository.save(transactionDestino);

        TransactionDTO transactionDTO = new TransactionDTO(transactionGuardada);

        return transactionDTO;
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Realizar pago de servicio.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public TransactionDTO makePayment(TransactionDepositDTO transactionDepositDTO, HttpServletRequest request) {

        // Obtener el usuario desde el token
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        // Buscar la cuenta del usuario (considerando tipo de cuenta del DTO)
        Optional<Account> userAccount = accountRepository.findByUserIdAndCurrency(userSecurityDTO.getId(),
                transactionDepositDTO.getCurrencyType());


        // Verificar que haya suficiente saldo en la cuenta del usuario
        if (userAccount.isEmpty() || userAccount.get().getBalance() < transactionDepositDTO.getAmount()
                || transactionDepositDTO.getAmount() > userAccount.get().getTransactionLimit()) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
        }

        // Registrar la transacción como payment
        Transaction payment = Transaction.builder()
                .amount(transactionDepositDTO.getAmount())
                .type(TransactionType.payment)
                .description(transactionDepositDTO.getDescription())
                .transactionDate(LocalDateTime.now())
                .account(userAccount.get())
                .accountDestino(null)// La cuenta del usuario
                .build();

        // Guardar la transacción
        transactionRepository.save(payment);

        // Actualizar el balance de la cuenta del usuario
        userAccount.get().setBalance(userAccount.get().getBalance() - transactionDepositDTO.getAmount());
        accountRepository.save(userAccount.get());


        // Preparar la respuesta
        TransactionDTO transactionDTO = new TransactionDTO(payment);

        return transactionDTO;
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Depositar dinero en una Cuenta.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public TransactionDTO deposit(HttpServletRequest request, TransactionDepositDTO transactionDepositDTO) {

        //Constructior del deposito
        Transaction deposito = Transaction.builder().build();

        //Extraemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        Optional<Account> cuenta;

        List<Account> listAccounts = accountRepository.getByIdUser(userSecurityDTO.getId());

        if (transactionDepositDTO.getCurrencyType().equals(CurrencyType.USD)) {
            cuenta = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.USD)
                    .findFirst();
            if (cuenta.isEmpty()) {
                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ACCOUNT_NO_ENCONTRADA);
            }
        } else {
            cuenta = listAccounts.stream()
                    .filter(account -> account.getCurrency() == CurrencyType.ARS)
                    .findFirst();
        }

        if (transactionDepositDTO.getAmount() <= 0) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_BALANCE_NEGATIVO);
        } else {

            double nuevoBalance = cuenta.get().getBalance() + transactionDepositDTO.getAmount();
            if (nuevoBalance > cuenta.get().getTransactionLimit()) {
                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_BALANCE_MAYOR_A_DEPOSITO);
            }
            cuenta.get().setBalance(nuevoBalance);
            accountRepository.save(cuenta.get());

            deposito.setAmount(transactionDepositDTO.getAmount());
            deposito.setType(TransactionType.deposit);
            deposito.setDescription(transactionDepositDTO.getDescription());
            deposito.setAccount(cuenta.get());
            deposito.setAccountDestino(null);

        }

        Transaction transactionGuardada = transactionRepository.save(deposito);

        TransactionDTO transactionDTO = new TransactionDTO(transactionGuardada);

        return transactionDTO;

    }
    //-----------------------------------------------------------------------------------------------------------

    //4- Actualizar la descripción de una transaccion.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public TransactionDTO updateTransactionDescription(Long transactionId, TransactionUpdateDTO transactionUpdateDTO, HttpServletRequest request) {

        Transaction transaction;

        //Obtener el usuario autenticado desde el token
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Obtenemos la lista de transacciones del usuario autenticado.
        List<Transaction> transactionList = transactionRepository.getTransactionsByIdUser(userSecurityDTO.getId());

        if (!transactionList.isEmpty()) {

            //Buscamos la transacción y verificamos que pertenezca al usuario autenticado.
            transaction = transactionList.stream()
                    .filter(trans -> trans.getId() == transactionId)
                    .findFirst()
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ERROR_TRANSACTION_NO_ENCONTRADA));

            //Actualizar la descripción
            transaction.setDescription(transactionUpdateDTO.getDescription());

            //Guardar la transacción actualizada
            transactionRepository.save(transaction);

        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_TRANSACTION_NOT_EXIST);
        }

        //Retornar la transacción actualizada como DTO
        return new TransactionDTO(transaction);
    }
    //-----------------------------------------------------------------------------------------------------------

    //5- Obtener listado de transacciones mediante ID de usuario.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public Page<TransactionDTO> getAllTransactionsFromUser(Long id, int page) {
        Pageable pageable = PageRequest.of(page, 10); // 10 elementos por página
        Page<Transaction> transactionPage = transactionRepository.getTransactionsByIdUserPageable(id, pageable);

        if (transactionPage.isEmpty()) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_TRANSACTION_NOT_EXIST);
        }

        return transactionPage.map(TransactionDTO::new);
    }
    //-----------------------------------------------------------------------------------------------------------

    //6- Obtener el detalle de una transaccion buscandola por ID.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public TransactionDTO transactionDetail(HttpServletRequest request, Long idTransaction) {

        //Autenticamos el usuario
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Buscamos una nueva transaction
        Transaction transaction = transactionRepository.findById(idTransaction)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.ERROR_NO_SE_ENCONTRO_ID_TRANSACTION));

        String toUpperCaseRole = userSecurityDTO.getRole();

        //Si el usuario no es administrador y la transaccion no le pertenece...
        if (!toUpperCaseRole.equals("ADMIN") && transaction.getAccount().getUser().getId()
                != userSecurityDTO.getId()) {

            throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.ERROR_TRANSACTION_NO_ENCONTRADA);
        }

        return new TransactionDTO(transaction);
    }
    //-----------------------------------------------------------------------------------------------------------
}
