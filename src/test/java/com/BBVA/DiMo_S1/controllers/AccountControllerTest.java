package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.AccountController;
import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowUpdateAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.UpdateAccountDTO;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
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
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountServiceImplementation accountServiceImplementation;

    @Mock
    private HttpServletRequest request;

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
    void testUpdateAccount_Success() {
        // Datos de prueba
        String cbu = "123456789";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(2000)
                .build();

        ShowUpdateAccountDTO expectedResponse = ShowUpdateAccountDTO.builder()
                .cbu(cbu)
                .currency(CurrencyType.ARS)
                .transactionLimit(2000)
                .balance(10000)
                .creationDate(LocalDateTime.now().minusMonths(1))
                .updateDate(LocalDateTime.now())
                .build();

        // Configuración de mocks
        Mockito.when(accountServiceImplementation.updateAccount(request, updateAccountDTO, cbu))
                .thenReturn(expectedResponse);

        // Llamada al método a probar
        ResponseEntity<ShowUpdateAccountDTO> response = accountController.updateAccount(request, updateAccountDTO, cbu);

        // Verificaciones
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testUpdateAccount_InvalidTransactionLimit() {
        // Datos de prueba
        String cbu = "123456789";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(500) // Límite inválido
                .build();

        // Configuración de mocks para lanzar excepción
        Mockito.when(accountServiceImplementation.updateAccount(request, updateAccountDTO, cbu))
                .thenThrow(new CustomException(HttpStatus.CONFLICT, ErrorConstants.LIMITE_TRANSACCION_INVALIDO));

        // Verificación de excepción
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            accountController.updateAccount(request, updateAccountDTO, cbu);
        });

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        Assertions.assertEquals(ErrorConstants.LIMITE_TRANSACCION_INVALIDO, exception.getMessage());
    }

    @Test
    void testUpdateAccount_InvalidCBU() {
        // Datos de prueba
        String cbu = "invalidCBU";
        UpdateAccountDTO updateAccountDTO = UpdateAccountDTO.builder()
                .transactionLimit(2000)
                .build();

        // Configuración de mocks para lanzar excepción
        Mockito.when(accountServiceImplementation.updateAccount(request, updateAccountDTO, cbu))
                .thenThrow(new CustomException(HttpStatus.CONFLICT, ErrorConstants.CBU_INVALIDO));

        // Verificación de excepción
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            accountController.updateAccount(request, updateAccountDTO, cbu);
        });

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        Assertions.assertEquals(ErrorConstants.CBU_INVALIDO, exception.getMessage());
    }
}
