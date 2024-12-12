package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.TransactionController;
import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.mocks.Mocks;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionsSendUsdTest {

    @Mock
    private TransactionServiceImplementation transactionServiceImplementation;

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
    void testTransactionSendUsdCorrecto(){
        System.out.println("Prueba de Amount Correcto");
        TransactionDTO transactionDTO = Mocks.transactionCompletaDTO();
        SimpleTransactionDTO simpleTransactionDTO = Mocks.simpleTransactionDTO();
        when(transactionServiceImplementation.sendMoney(httpServletRequest,simpleTransactionDTO)).thenReturn(transactionDTO);
        ResponseEntity<TransactionDTO> response = transactionController.sendUsd(simpleTransactionDTO,httpServletRequest);
        assertNotNull(response);
        System.out.println(response);
        assertEquals(HttpStatusCode.valueOf(201),response.getStatusCode());
    }
}
