package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowUpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.UpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
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
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImplementation accountServiceImplementation;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtService jwtService;

    private HttpServletRequest mockRequest;

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
