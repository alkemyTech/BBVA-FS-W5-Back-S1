package com.BBVA.DiMo_S1.services;

import com.BBVA.DiMo_S1.B_services.implementations.AccountServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.AuthServiceImplementation;
import com.BBVA.DiMo_S1.B_services.implementations.RoleServiceImplementation;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.ShowCreatedAccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.LoginUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import com.BBVA.DiMo_S1.D_models.Role;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.mocks.MocksUserAuth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    RoleServiceImplementation roleServiceImplementation;

    @Mock
    AccountServiceImplementation accountServiceImplementation;

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

        Authentication authentication = mock(Authentication.class);

        // Configurar el comportamiento del mock
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmailAndSoftDeleteIsNull(loginUserDTO.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().getName())).thenReturn("mockToken");

        // Llamar al método del servicio
        ShowCreatedUserDTO response = authServiceImplementation.login(loginUserDTO.getEmail(), loginUserDTO.getPassword());

        // Verificar el resultado
        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
    }
    @Test
    void testRegisterService(){
        // Datos de prueba
        CreateUserDTO createUserDTO = MocksUserAuth.createMockUserDTO();
        User user = User.builder().build();
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());
        user.setSoftDelete(null);  // Asegurar que el usuario no está desactivado
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        role.setDescription("admin");
        role.setCreationDate(LocalDateTime.now());
        user.setRole(role);

        // Mock del usuario guardado con ID y softDelete null
        User savedUser = User.builder()
                .id(1L)
                .email(createUserDTO.getEmail())
                .password("encodedPassword")
                .softDelete(null)  // Asegurar que el usuario no está desactivado
                .role(role)
                .build();

        // Configurar el comportamiento del mock
        when(userRepository.findByEmail(createUserDTO.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(createUserDTO.getPassword())).thenReturn("encodedPassword");
        when(roleServiceImplementation.findById(2L)).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.findByEmailAndSoftDeleteIsNull(savedUser.getEmail())).thenReturn(Optional.of(savedUser));
        when(jwtService.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().getName())).thenReturn("mockToken");

        // Llamar al método del servicio
        ShowCreatedUserDTO response = authServiceImplementation.createUser(createUserDTO);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
    }
}
