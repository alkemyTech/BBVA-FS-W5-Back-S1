package com.BBVA.DiMo_S1.services;



import com.BBVA.DiMo_S1.B_services.implementations.FixedTermDepositServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.ShowSimulatedFixedTermDeposit;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.BBVA.DiMo_S1.E_config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class FixedTermDepositImplTest {

    @InjectMocks
    private FixedTermDepositServiceImplementation fixedTermDepositServiceImplementation;

    @Mock
    private FixedTermDepositRepository fixedTermDepositRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtService jwtService;

    @Test
    void testCreateFixedTermDeposit_Success() {
        // Mock del HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        // DTO de entrada
        CreateFixedTermDepositDTO createFixedTermDepositDTO = CreateFixedTermDepositDTO.builder()
                .amount(2000.0) // Monto válido
                .cantidadDias(30) // Duración válida
                .build();

        // Mock de UserSecurityDTO retornado por JwtService
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("mockToken");
        Mockito.when(jwtService.validateAndGetSecurity(eq("mockToken"))).thenReturn(
                new UserSecurityDTO(1L, "testUser@test.com", "USER", LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );

        // Mock de Account retornada por el repositorio
        Account mockAccount = Account.builder()
                .id(1L)
                .balance(5000.0) // Balance suficiente
                .build();
        Mockito.when(accountRepository.getArsAccountByIdUser(eq(1L))).thenReturn(Optional.of(mockAccount));

        // Mock de FixedTermDepositRepository para guardar el plazo fijo
        Mockito.when(fixedTermDepositRepository.save(any(FixedTermDeposit.class))).thenAnswer(invocation -> {
            FixedTermDeposit deposit = invocation.getArgument(0);
            deposit.setId(1L); // Simular ID generado al guardar
            return deposit;
        });

        // Simular URI no de simulación
        Mockito.when(mockRequest.getRequestURI()).thenReturn("/api/fixedtermdeposit");

        // Llamar al metodo bajo prueba
        ShowSimulatedFixedTermDeposit response = fixedTermDepositServiceImplementation.createFixedTermDeposit(mockRequest, createFixedTermDepositDTO);

        // Verificar respuesta
        assertNotNull(response);
        assertEquals(createFixedTermDepositDTO.getAmount(), response.getAmount());
        assertEquals("2%", response.getInterest());
        assertFalse(response.isSettled());
        assertNotNull(response.getCreationDate());
        assertNotNull(response.getClosingDate());

        // Verificar mocks
        Mockito.verify(jwtService, Mockito.times(1)).validateAndGetSecurity(eq("mockToken"));
        Mockito.verify(accountRepository, Mockito.times(1)).getArsAccountByIdUser(eq(1L));
        Mockito.verify(fixedTermDepositRepository, Mockito.times(1)).save(any(FixedTermDeposit.class));
    }
}
