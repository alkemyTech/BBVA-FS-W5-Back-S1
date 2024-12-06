package com.BBVA.DiMo_S1.integration;

import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class RegisterAndGetTokenIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registrarseYobtenerToken() throws Exception {

        CreateUserDTO createUserDTO = CreateUserDTO.builder().build();

        createUserDTO.setFirstName("nombrePrueba");
        createUserDTO.setLastName("apellidoPrueba");
        createUserDTO.setEmail("emailPrueba@gmail.com");
        createUserDTO.setPassword("passwordPrueba");


        String userJson = objectMapper.writeValueAsString(createUserDTO);


        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String token = result.getResponse().getContentAsString();
        System.out.println("Token: " + token);


        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            throw new AssertionError("El token no es un JWT válido.");
        }
    }
}
