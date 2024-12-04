package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.TransactionController;
import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.Mocks.MocksTransaction;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class transactionControllerTest {

    @Mock
    TransactionServiceImplementation transactionServiceImplementation;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    private AutoCloseable autoCloseable;

    @Test
    void testTransactionSendArsCorrecto(){
        //Datos de prueba
        TransactionDTO transactionDTO = new TransactionDTO();
        SimpleTransactionDTO simpleTransactionDTO = MocksTransaction.mockSimpleTransactionDTO();
        //Configurar el comportamiento del mock(usamos el servicio del controller)
        when(transactionServiceImplementation.sendMoney(httpServletRequest,simpleTransactionDTO)).thenReturn(transactionDTO);
        //Llamar al metodo del controlador
        ResponseEntity<TransactionDTO> response = transactionController.sendUsd(simpleTransactionDTO,httpServletRequest);
        // Verificar el resultado
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(201),response.getStatusCode());
    }
}
