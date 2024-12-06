package com.BBVA.DiMo_S1.integration;

import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.D_models.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RoleIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void crearRoleYverificarCreacion() {

        Role role = Role.builder().build();
        Optional<Role> savedRole = null;

        //Creamos rol con sus campos completos.
        role.setName("USER1");
        role.setDescription("Role de usuario");

        roleRepository.save(role);

        savedRole = Optional.ofNullable(roleRepository.findByName("USER1"));
        assertTrue(savedRole.isPresent());
        assertEquals("USER1", savedRole.get().getName());

        //Creamos rol solo con su nombre. Descripcion opcional.
        role.setName("USER2");

        roleRepository.save(role);

        savedRole = Optional.ofNullable(roleRepository.findByName("USER2"));
        assertTrue(savedRole.isPresent());
        assertEquals("USER2", savedRole.get().getName());
    }

    @Test
    public void timeStampsSeGeneranCorrectamente() {

        //Inicializamos al role.
        Role role = Role.builder().build();

        //Creamos rol con sus campos completos.
        role.setName("USER1");
        role.setDescription("Role de usuario");

        roleRepository.save(role);

        assertNotNull(role.getCreationDate());
        assertNotNull(role.getUpdateDate());
    }
}