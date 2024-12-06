package com.BBVA.DiMo_S1.controllers;

import com.BBVA.DiMo_S1.A_controllers.UserController;
import com.BBVA.DiMo_S1.B_services.implementations.UserServiceImplementation;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImplementation userServiceImplementation;

    @Mock
    private JwtService jwtService;

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
    void userUpdate_success() {

        //Datos de prueba
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("UpdatedName");
        updateUserDTO.setLastName("UpdatedLastName");
        updateUserDTO.setPassword("ValidPassword");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer mockToken");

        //Configuración de mock
        Mockito.when(userServiceImplementation.updateUser(any(HttpServletRequest.class), any(UpdateUserDTO.class)))
                .thenReturn(updateUserDTO);

        //Ejecución
        ResponseEntity<UpdateUserDTO> response = userController.userUpdate(mockRequest, updateUserDTO);

        //Verificaciones
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("UpdatedName", response.getBody().getFirstName());
        Assertions.assertEquals("UpdatedLastName", response.getBody().getLastName());
    }

    @Test
    void userUpdate_userNotFound_throwsException() {

        //Datos de prueba
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("UpdatedName");
        updateUserDTO.setLastName("UpdatedLastName");
        updateUserDTO.setPassword("ValidPassword");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer mockToken");

        //Configuración de mock
        Mockito.when(userServiceImplementation.updateUser(any(HttpServletRequest.class), any(UpdateUserDTO.class)))
                .thenThrow(new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO));

        //Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                userController.userUpdate(mockRequest, updateUserDTO));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.USER_NO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void userUpdate_invalidPassword_throwsException() {

        //Datos de prueba
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("UpdatedName");
        updateUserDTO.setLastName("UpdatedLastName");
        updateUserDTO.setPassword("123");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer mockToken");

        //Configuración de mock
        Mockito.when(userServiceImplementation.updateUser(any(HttpServletRequest.class), any(UpdateUserDTO.class)))
                .thenThrow(new CustomException(HttpStatus.CONFLICT, ErrorConstants.CONTRASEÑA_INVALIDA));

        //Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                userController.userUpdate(mockRequest, updateUserDTO));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.CONTRASEÑA_INVALIDA, exception.getMessage());
    }
}
