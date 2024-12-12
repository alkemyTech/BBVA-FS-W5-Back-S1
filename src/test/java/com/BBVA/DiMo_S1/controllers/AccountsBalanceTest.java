package com.BBVA.DiMo_S1.controllers;


import com.BBVA.DiMo_S1.A_controllers.AccountController;
import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountsBalanceTest {

    @Mock
    private AccountServiceImplementation accountServiceImplementation;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AccountController accountController;

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
    void testAccountBalance(){
        System.out.println("Prueba de Account Correcto");
        BalanceDto balanceDto = Mocks.balanceDto();
        when(accountServiceImplementation.obtainBalance(httpServletRequest)).thenReturn(balanceDto);
        ResponseEntity<BalanceDto> response = accountController.obtainBalance(httpServletRequest);
        assertNotNull(response);
        System.out.println(response);
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
    }



}
