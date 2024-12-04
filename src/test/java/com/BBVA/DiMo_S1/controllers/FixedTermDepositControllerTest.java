package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.FixedTermDepositController;
import com.BBVA.DiMo_S1.B_services.implementations.FixedTermDepositServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.ShowSimulatedFixedTermDeposit;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class FixedTermDepositControllerTest {

    @Mock
    private FixedTermDepositServiceImplementation fixedTermDepositServiceImplementation;

    @InjectMocks
    private FixedTermDepositController fixedTermDepositController;

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
    void testCrearPlazoFijo_RespuestaCorrecta() {
        // Mock del HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        // DTO de entrada
        CreateFixedTermDepositDTO inputDTO = CreateFixedTermDepositDTO.builder()
                .amount(1000.0)
                .cantidadDias(30)
                .build();

        // DTO de salida esperado
        ShowSimulatedFixedTermDeposit expectedResponse = ShowSimulatedFixedTermDeposit.builder()
                .amount(1000.0)
                .interest("2%")
                .creationDate(LocalDateTime.now())
                .closingDate(LocalDateTime.now().plusDays(30))
                .settled(false)
                .build();

        // Configurar el comportamiento del servicio simulado
        Mockito.when(fixedTermDepositServiceImplementation.createFixedTermDeposit(eq(mockRequest), eq(inputDTO)))
                .thenReturn(expectedResponse);

        // Llamar al metodo del controlador
        ResponseEntity<ShowSimulatedFixedTermDeposit> response = fixedTermDepositController.createFixedTermDeposit(mockRequest, inputDTO);

        // Verificar el resultado
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        // Verificar que el servicio fue llamado correctamente
        Mockito.verify(fixedTermDepositServiceImplementation, Mockito.times(1))
                .createFixedTermDeposit(eq(mockRequest), eq(inputDTO));
    }

    @Test
    void testCrearPlazoFijo_IdInexistente() {
        // Mock del HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        // DTO de entrada
        CreateFixedTermDepositDTO inputDTO = CreateFixedTermDepositDTO.builder()
                .amount(1000.0)
                .cantidadDias(30)
                .build();

        // Configurar el comportamiento del servicio para lanzar una excepción
        Mockito.when(fixedTermDepositServiceImplementation.createFixedTermDeposit(eq(mockRequest), eq(inputDTO)))
                .thenThrow(new CustomException(HttpStatus.NOT_FOUND, "ID inexistente"));

        // Llamar al metodo del controlador y verificar el error
        CustomException exception = assertThrows(CustomException.class, () ->
                fixedTermDepositController.createFixedTermDeposit(mockRequest, inputDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("ID inexistente", exception.getMessage());
    }

    @Test
    void testCrearPlazoFijo_ErrorDePermisos() {
        // Mock del HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        // DTO de entrada
        CreateFixedTermDepositDTO inputDTO = CreateFixedTermDepositDTO.builder()
                .amount(1000.0)
                .cantidadDias(30)
                .build();

        // Configurar el comportamiento del servicio para lanzar una excepción
        Mockito.when(fixedTermDepositServiceImplementation.createFixedTermDeposit(eq(mockRequest), eq(inputDTO)))
                .thenThrow(new CustomException(HttpStatus.FORBIDDEN, "No tiene permisos para realizar esta acción"));

        // Llamar al método del controlador y verificar el error
        CustomException exception = assertThrows(CustomException.class, () ->
                fixedTermDepositController.createFixedTermDeposit(mockRequest, inputDTO));

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertEquals("No tiene permisos para realizar esta acción", exception.getMessage());
    }

    @Test
    void testCrearPlazoFijo_ErrorDeValidacion() {
        // Mock del HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        // DTO de entrada inválido
        CreateFixedTermDepositDTO inputDTO = CreateFixedTermDepositDTO.builder()
                .amount(-1000.0) // Valor inválido
                .cantidadDias(0) // Duración inválida
                .build();

        // Configurar el comportamiento del servicio para lanzar una excepción
        Mockito.when(fixedTermDepositServiceImplementation.createFixedTermDeposit(eq(mockRequest), eq(inputDTO)))
                .thenThrow(new CustomException(HttpStatus.BAD_REQUEST, "Errores de validación"));

        // Llamar al metodo del controller y verificar el error
        CustomException exception = assertThrows(CustomException.class, () ->
                fixedTermDepositController.createFixedTermDeposit(mockRequest, inputDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals("Errores de validación", exception.getMessage());
    }
}