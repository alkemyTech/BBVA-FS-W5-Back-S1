package com.BBVA.DiMo_S1.integration;

import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void crearUsuarioYverificarCreacion() {
        //Inicializamos al usuario.
        User user = User.builder().build();

        //Seteamos los datos.
        user.setFirstName("Juan");
        user.setLastName("Noli");
        user.setEmail("JuanNoli@gmail.com");
        user.setPassword("123456");
        user.setRole(roleRepository.findById(1l).get());

        //Inicializamos la cuenta del usuario..
        Account account = Account.builder().build();

        //Seteamos los datos.
        account.setCbu("14856120525550541548650");
        account.setCurrency(CurrencyType.ARS);
        account.setTransactionLimit(3000000);
        account.setBalance(0);
        account.setUser(user);

        //Persistimos al user y su account en la BD.
        userRepository.save(user);
        accountRepository.save(account);

        //Buscamos al usuario y la cuenta en la BD para ver si se guardo éxitosamente.
        Optional<User> savedUser = userRepository.findByEmail("JuanNoli@gmail.com");
        Optional<Account> savedAccount = accountRepository.findByUserIdAndCurrency(user.getId(), CurrencyType.ARS);
        assertTrue(savedAccount.isPresent());
        assertTrue(savedUser.isPresent());
        assertEquals("Juan", savedUser.get().getFirstName());
        assertEquals("14856120525550541548650", savedAccount.get().getCbu());
    }

    @Test
    public void guardarUsuariosConMismoEmail() {
        //Inicializamos al usuario.
        User user1 = User.builder().build();

        //Seteamos los datos.
        user1.setFirstName("Juan");
        user1.setLastName("Noli");
        user1.setEmail("JuanNoli@gmail.com");
        user1.setPassword("123456");
        user1.setRole((roleRepository.findById(1l)).get());

        //Inicializamos al usuario.
        User user2 = User.builder().build();

        //Seteamos los datos.
        user2.setFirstName("Juan");
        user2.setLastName("Noli");
        user2.setEmail("JuanNoli@gmail.com");
        user2.setPassword("123456");
        user2.setRole((roleRepository.findById(1l)).get());

        //Guardamos al usuario.
        userRepository.save(user1);

        //Probamos la excepción en caso de que se quiera agregar a un mismo usuario con el mismo mail.
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));
    }

    @Test
    public void timeStampsSeGeneranCorrectamente() {
        //Inicializamos al usuario.
        User user = User.builder().build();

        //Seteamos los datos.
        user.setFirstName("Juan");
        user.setLastName("Noli");
        user.setEmail("JuanNoli@gmail.com");
        user.setPassword("123456");
        user.setRole((roleRepository.findById(1l)).get());

        userRepository.save(user);

        assertNotNull(user.getCreationDate());
    }

    @Test
    public void darDeBajaUsuarioYverificarEliminacion() throws Exception {

        //Inicializamos al usuario.
        User user = User.builder().build();

        //Seteamos los datos.
        user.setFirstName("Juan");
        user.setLastName("Noli");
        user.setEmail("juanNol@gmail.com");
        user.setPassword("123456");
        user.setRole((roleRepository.findById(1l)).get());
        user.setSoftDelete(LocalDateTime.now());

        userRepository.save(user);

        //Generamos token de autenticacion.
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().getName());

        //Simulamos que el usuario esta autentiaco y le pegamos al endpoint del controller.
        mockMvc.perform(delete("/users") // El endpoint de tu soft delete
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json"))
                .andExpect(status().isOk()); // Esperamos que la respuesta sea 200 OK

        //Verificamos que el usuario haya sido eliminado con éxito.
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isPresent());
        assertNotNull(deletedUser.get().getSoftDelete());

        //Verificamos que sus cuentas tambíen.
        List<Account> accounts = accountRepository.getByIdUser(user.getId());
        for (Account account : accounts) {
            assertNotNull(account.getSoftDelete(), "El campo softDelete de la cuenta debería estar marcado.");
        }
    }
}
