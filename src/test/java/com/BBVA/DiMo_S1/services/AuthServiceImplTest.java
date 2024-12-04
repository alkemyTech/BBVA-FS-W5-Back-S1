package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.LoginUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import com.BBVA.DiMo_S1.D_models.Role;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.Mocks.MocksUserAuth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImplementation authServiceImplementation;

    private AutoCloseable autoCloseable;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void loginServiceTest(){
        // Datos de prueba
        LoginUserDTO loginUserDTO = MocksUserAuth.loginMockUserDTO();
        User user = new User();
        user.setEmail(loginUserDTO.getEmail());
        user.setPassword(loginUserDTO.getPassword());
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        role.setDescription("admin");
        role.setCreationDate(LocalDateTime.now());
        user.setRole(role);

        // Configurar el comportamiento del mock
        when(userRepository.findByEmailAndSoftDeleteIsNull(loginUserDTO.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().getName())).thenReturn("mockToken");

        // Llamar al método del servicio
        ShowCreatedUserDTO response = authServiceImplementation.login(loginUserDTO.getEmail(), loginUserDTO.getPassword());

        // Verificar el resultado
        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
    }

}
