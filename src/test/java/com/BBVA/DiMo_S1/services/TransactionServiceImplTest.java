package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TransactionServiceImplementation transactionServiceImplementation;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSendMoneySuccess() {
        // Datos de prueba
        HttpServletRequest request = mock(HttpServletRequest.class);
        SimpleTransactionDTO simpleTransactionDTO = new SimpleTransactionDTO();
        simpleTransactionDTO.setAmount(100d);
        simpleTransactionDTO.setCBU("1234567890");
        simpleTransactionDTO.setDescription("Test transaction");

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO();
        userSecurityDTO.setId(1L);
        when(jwtService.validateAndGetSecurity(anyString())).thenReturn(userSecurityDTO);
        when(jwtService.extractToken(request)).thenReturn("token");

        List<Account> listAccounts = new ArrayList<>();
        Account accountEmisora = new Account();
        accountEmisora.setCurrency(CurrencyType.USD);
        accountEmisora.setBalance(1000);
        accountEmisora.setTransactionLimit(500);
        User userEmisor = new User();
        userEmisor.setId(1L);
        accountEmisora.setUser(userEmisor);
        listAccounts.add(accountEmisora);
        when(accountRepository.getByIdUser(userSecurityDTO.getId())).thenReturn(listAccounts);
        when(request.getRequestURI()).thenReturn("transactions/sendUsd");

        Account accountDestino = new Account();
        accountDestino.setCurrency(CurrencyType.USD);
        accountDestino.setBalance(500);
        accountDestino.setSoftDelete(null);
        User userDestino = new User();
        userDestino.setId(2L);
        userDestino.setFirstName("John");
        accountDestino.setUser(userDestino);

        when(accountRepository.findByCbu(simpleTransactionDTO.getCBU())).thenReturn(Optional.of(accountDestino));

        // Mock para salvar las cuentas y transacciones
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamar al metodo del servicio
        TransactionDTO response = transactionServiceImplementation.sendMoney(request, simpleTransactionDTO);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals(simpleTransactionDTO.getAmount(), response.getAmount());
    }
}
