package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.A_controllers.AccountController;
import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.mocks.Mocks;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AccountServiceImplTest {

    @Mock
    private FixedTermDepositRepository fixedTermDepositRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountServiceImplementation accountServiceImplementation;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountController accountController;

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
    void testAccountBalance() {
        System.out.println("Prueba de Account Correcto");
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Crear un BalanceDto de prueba
        BalanceDto balanceDto = Mocks.balanceDto();

        // Simular el comportamiento del servicio jwtService y el repositorio
        UserSecurityDTO userSecurityDTO = new UserSecurityDTO();
        userSecurityDTO.setId(1L);
        when(jwtService.validateAndGetSecurity(anyString())).thenReturn(userSecurityDTO);
        when(jwtService.extractToken(request)).thenReturn("token");

        List<Account> listAccounts = new ArrayList<>();
        Account account = new Account();
        account.setCurrency(CurrencyType.ARS);
        account.setBalance(1000.0);
        User user = new User();
        user.setId(1L);
        account.setUser(user);
        listAccounts.add(account);
        when(accountRepository.getByIdUser(userSecurityDTO.getId())).thenReturn(listAccounts);

        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setAmount(200.0);
        transactionList.add(transaction);
        when(transactionRepository.getTransactionsByIdUserPageable(userSecurityDTO.getId(), Pageable.unpaged())).thenReturn(new PageImpl<>(transactionList));

        List<FixedTermDeposit> fixedTermDeposits = new ArrayList<>();
        FixedTermDeposit fixedTermDeposit = new FixedTermDeposit();
        fixedTermDeposits.add(fixedTermDeposit);
        when(fixedTermDepositRepository.getFixedTermDepositByIdUser(userSecurityDTO.getId())).thenReturn(fixedTermDeposits);

        // Simular la respuesta del método del servicio
        when(accountServiceImplementation.obtainBalance(request)).thenReturn(balanceDto);

        // Llamar al método del controlador
        ResponseEntity<BalanceDto> response = accountController.obtainBalance(request);

        // Verificar el resultado
        assertNotNull(response);
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
