package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.B_services.implementations.UserServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UpdateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
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
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImplementation userServiceImplementation;

    @Mock
    private UserRepository userRepository;

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
    void updateUser_success() {
        // Datos de prueba
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .password("SecurePass123")
                .build();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(1L, "user@test.com", "ROLE_USER",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        User user = User.builder()
                .id(userSecurityDTO.getId())
                .firstName("OldName")
                .lastName("OldLastName")
                .email(userSecurityDTO.getEmail())
                .creationDate(userSecurityDTO.getCreatedAt())
                .password("OldPassword")
                .build();

        // Configuración de mocks
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("dummy-token");
        Mockito.when(jwtService.validateAndGetSecurity("dummy-token")).thenReturn(userSecurityDTO);
        Mockito.when(userRepository.findById(userSecurityDTO.getId())).thenReturn(Optional.of(user));

        // Ejecución del metodo
        UpdateUserDTO result = userServiceImplementation.updateUser(mockRequest, updateUserDTO);

        // Verificaciones
        Assertions.assertNotNull(result);
        Assertions.assertEquals("John", user.getFirstName());
        Assertions.assertEquals("Doe", user.getLastName());
        Assertions.assertTrue(BCrypt.checkpw("SecurePass123", user.getPassword()));
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void updateUser_passwordInvalid_throwsException() {
        // Datos de prueba
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .password("123") // Contraseña no válida (menos de 6 caracteres)
                .build();

        // Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                userServiceImplementation.updateUser(mockRequest, updateUserDTO));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.CONTRASEÑA_INVALIDA, exception.getMessage());
    }

    @Test
    void updateUser_userNotFound_throwsException() {
        // Datos de prueba
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .password("SecurePass123")
                .build();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(1L, "user@test.com", "ROLE_USER",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        // Configuración de mocks
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("dummy-token");
        Mockito.when(jwtService.validateAndGetSecurity("dummy-token")).thenReturn(userSecurityDTO);
        Mockito.when(userRepository.findById(userSecurityDTO.getId())).thenReturn(Optional.empty());

        // Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                userServiceImplementation.updateUser(mockRequest, updateUserDTO));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.USER_NO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void updateUser_jwtValidationFails_throwsException() {
        // Datos de prueba
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .password("SecurePass123")
                .build();

        // Configuración de mocks
        Mockito.when(jwtService.extractToken(mockRequest)).thenReturn("invalid-token");
        Mockito.when(jwtService.validateAndGetSecurity("invalid-token"))
                .thenThrow(new CustomException(HttpStatus.UNAUTHORIZED, ErrorConstants.TOKEN_INVALIDO));

        // Ejecución y verificación
        CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                userServiceImplementation.updateUser(mockRequest, updateUserDTO));

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assertions.assertEquals(ErrorConstants.TOKEN_INVALIDO, exception.getMessage());
    }
}
