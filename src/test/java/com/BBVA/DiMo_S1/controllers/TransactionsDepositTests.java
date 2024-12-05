package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.TransactionController;
import com.BBVA.DiMo_S1.B_services.implementations.TransactionServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionCompletaDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import com.BBVA.DiMo_S1.mocks.Mocks;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionsDepositTests {


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
    void depositTest(){
        System.out.println("Prueba de Transaction Incorrecto");
        TransactionDepositDTO transactionDepositDTO = Mocks.transactionDepositDTO();
        TransactionDTO transactionCompletaDTO = Mocks.transactionCompletaDTO();

        when(transactionServiceImplementation.deposit(httpServletRequest, transactionDepositDTO)).thenReturn(transactionCompletaDTO);

        ResponseEntity<TransactionDTO> response = transactionController.deposit(transactionDepositDTO,httpServletRequest);
        assertNotNull(response);
        System.out.println(response);
        assertEquals(HttpStatusCode.valueOf(201),response.getStatusCode());
    }

}
