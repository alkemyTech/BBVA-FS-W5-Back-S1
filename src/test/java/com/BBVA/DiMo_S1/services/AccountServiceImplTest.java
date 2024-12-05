package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.A_controllers.AccountController;
import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowUpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.UpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.mocks.Mocks;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImplementation accountServiceImplementation;
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

    private HttpServletRequest mockRequest;
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

    @Test
    void updateAccount_success() {
        // Datos de prueba
        String cbu = "123456789";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(2000)
                .build();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(1L, "user@test.com", "ROLE_USER",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        Account account = Account.builder()
                .id(1L)
                .cbu(cbu)
                .balance(5000)
                .transactionLimit(1500)
                .creationDate(LocalDateTime.now())
                .user(User.builder().id(1L).build())
                .build();

        // Configuración de mocks
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("dummy-token");
        Mockito.when(jwtService.validateAndGetSecurity("dummy-token")).thenReturn(userSecurityDTO);
        Mockito.when(accountRepository.findByCbu(cbu)).thenReturn(Optional.of(account));

        // Ejecución del metodo
        ShowUpdateAccountDTO result = accountServiceImplementation.updateAccount(mockRequest, updateAccountDTO, cbu);

        // Verificaciones
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2000, result.getTransactionLimit());
        Mockito.verify(accountRepository).save(account);
    }

    @Test
    void updateAccount_invalidTransactionLimit_throwsException() {
        // Datos de prueba
        String cbu = "123456789";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(500) // Límite no válido
                .build();

        // Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                accountServiceImplementation.updateAccount(mockRequest, updateAccountDTO, cbu));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.LIMITE_TRANSACCION_INVALIDO, exception.getMessage());
    }

    @Test
    void updateAccount_accountNotFound_throwsException() {
        // Datos de prueba
        String cbu = "123456789";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(2000)
                .build();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(1L, "user@test.com", "ROLE_USER",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        // Configuración de mocks
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("dummy-token");
        Mockito.when(jwtService.validateAndGetSecurity("dummy-token")).thenReturn(userSecurityDTO);
        Mockito.when(accountRepository.findByCbu(cbu)).thenReturn(Optional.empty());

        // Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                accountServiceImplementation.updateAccount(mockRequest, updateAccountDTO, cbu));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.CBU_INVALIDO, exception.getMessage());
    }

    @Test
    void updateAccount_accountNotBelongsToUser_throwsException() {
        // Datos de prueba
        String cbu = "123456789";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(2000)
                .build();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(1L, "user@test.com", "ROLE_USER",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        Account account = Account.builder()
                .id(1L)
                .cbu(cbu)
                .balance(5000)
                .transactionLimit(1500)
                .creationDate(LocalDateTime.now())
                .user(User.builder().id(2L).build()) // Cuenta no pertenece al usuario autenticado
                .build();

        // Configuración de mocks
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("dummy-token");
        Mockito.when(jwtService.validateAndGetSecurity("dummy-token")).thenReturn(userSecurityDTO);
        Mockito.when(accountRepository.findByCbu(cbu)).thenReturn(Optional.of(account));

        // Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                accountServiceImplementation.updateAccount(mockRequest, updateAccountDTO, cbu));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.CBU_INVALIDO, exception.getMessage());
    }


}
