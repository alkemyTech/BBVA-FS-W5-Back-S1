package com.BBVA.DiMo_S1.integration;

import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class GetUsersByAdminIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void mostrarUsuariosByAdminConExito() throws Exception {

        //Obtenemos un usuario admin de la BD.
        Optional<User> user = userRepository.findByEmail("p1@gmail.com");

        //Generamos token de autenticacion.
        String token = jwtService.generateToken(user.get().getId(), user.get().getEmail(),
                user.get().getRole().getName());

        mockMvc.perform(get("/users/admin")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void mostrarUsuariosByUserFrobidden() throws Exception {

        //Obtenemos un usuario user de la BD.
        Optional<User> user = userRepository.findByEmail("p11@gmail.com");

        //Generamos token de autenticacion.
        String token = jwtService.generateToken(user.get().getId(), user.get().getEmail(),
                user.get().getRole().getName());

        mockMvc.perform(get("/users/admin")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json"))
                .andExpect(status().isConflict());
    }
}
