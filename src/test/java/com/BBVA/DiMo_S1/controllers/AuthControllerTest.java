package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.AuthController;
import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.LoginUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import com.BBVA.DiMo_S1.mocks.MocksUserAuth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthControllerTest {

    @Mock
    private AuthServiceImplementation authServiceImplementation;
    @InjectMocks
    private AuthController authController;

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
    void testRegister(){
        //Datos de prueba
        CreateUserDTO createUserDTO = MocksUserAuth.createMockUserDTO();
        ShowCreatedUserDTO showCreatedUserDTO = new ShowCreatedUserDTO();
        //Configurar el comportamiento del mock(usamos el servicio del controller)
        when(authServiceImplementation.createUser(createUserDTO)).thenReturn(showCreatedUserDTO);
        //Llamar al metodo del controlador
        ResponseEntity<ShowCreatedUserDTO> response = authController.registerUser(createUserDTO);
        // Verificar el resultado
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }
    @Test
    void testLogin(){
        //Datos de prueba
        LoginUserDTO loginUserDTO = MocksUserAuth.loginMockUserDTO();
        ShowCreatedUserDTO showCreatedUserDTO = new ShowCreatedUserDTO();
        //Configurar el comportamiento del mock(usamos el servicio del controller)
        when(authServiceImplementation.login(loginUserDTO.getEmail(),loginUserDTO.getPassword())).thenReturn(showCreatedUserDTO);
        //Llamar al metodo del controlador
        ResponseEntity<ShowCreatedUserDTO> response = authController.login(loginUserDTO);
        // Verificar el resultado
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

}
