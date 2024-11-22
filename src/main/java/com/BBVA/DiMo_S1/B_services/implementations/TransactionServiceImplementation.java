package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.D_dtos.TransactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_models.Enums.TransactionType;
import com.BBVA.DiMo_S1.D_models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplementation implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImplementation(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

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
        var recipientAccount = accountRepository.findById(transactionDTO.getAccountId())
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

}
