package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public void sendArs(TransactionDTO transactionDTO, String token) {
        // Obtener el ID del usuario emisor del token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Verificar que el emisor exista y tenga balance suficiente
        var senderAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta emisora no encontrada."));
        if (senderAccount.getBalance() < transactionDTO.getAmount()) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        if (transactionDTO.getAmount() > senderAccount.getTransactionLimit()) {
            throw new IllegalArgumentException("El monto excede el límite permitido.");
        }

        // Verificar que la cuenta destinataria exista
        var recipientAccount = accountRepository.findByCbu(transactionDTO.getCuentaDestino())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta receptora no encontrada."));

        // Crear y guardar la transacción de "payment" para el emisor
        Transaction senderTransaction = Transaction.builder()
                .amount(transactionDTO.getAmount())
                .type(TransactionType.payment)
                .description(transactionDTO.getDescription())
                .transactionDate(transactionDTO.getTransactionDate())
                .account(senderAccount)
                .build();
        transactionRepository.save(senderTransaction);

    // Crear y guardar la transacción de "income" para el receptor
    Transaction recipientTransaction = Transaction.builder()
            .amount(transactionDTO.getAmount())
            .type(TransactionType.deposit)
            .description("Transferencia recibida de " + email)
            .transactionDate(transactionDTO.getTransactionDate())
            .account(recipientAccount)
            .build();
        transactionRepository.save(recipientTransaction);

    // Actualizar los balances de las cuentas
        senderAccount.setBalance(senderAccount.getBalance() - transactionDTO.getAmount());
        recipientAccount.setBalance(recipientAccount.getBalance() + transactionDTO.getAmount());
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);
}

    public TransactionDTO sendUsd (HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO) {

        //Inicializamos la transaccion vacia.
        Transaction transaction = Transaction.builder().build();

        //Extraemos el User autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        //Antes de realizar cualquier tipo de validación, validamos: Si el User autenticado tiene cuenta en USD,
        //si el balance es suficiente y si el monto es correcto.
        Optional <Account> accountEmisora = accountRepository.getUsdAccountByIdUser(userSecurityDTO.getId());

        if (accountEmisora.isEmpty() || simpleTransactionDTO.getAmount() > accountEmisora.get().getTransactionLimit()
        || accountEmisora.get().getBalance() < simpleTransactionDTO.getAmount()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_EN_TRANSACCION);
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
                if (accountDestino.getCurrency().equals(CurrencyType.USD)) {

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
                    transaction.setDescription(simpleTransactionDTO.getDescription());
                    transaction.setAmount(simpleTransactionDTO.getAmount());
                    transaction.setType(TransactionType.payment);
                    transaction.setAccount(accountDestino);

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
        Transaction transactionGuardada = transactionRepository.save(transaction);

        TransactionDTO transactionDTO = new TransactionDTO(transactionGuardada);

        return transactionDTO;
    }
}
