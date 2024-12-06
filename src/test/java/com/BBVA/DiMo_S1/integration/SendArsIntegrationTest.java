package com.BBVA.DiMo_S1.integration;

import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class SendArsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void enviarArsYcrearTransaccion() throws Exception {

        //Obtenemos un usuario de la BD.
        Optional<User> user = userRepository.findByEmail("p12@gmail.com");

        //Generamos token de autenticacion.
        String token = jwtService.generateToken(user.get().getId(), user.get().getEmail(),
                user.get().getRole().getName());

        //Obtenemos 2 cuentas que van a funcionar como emisoras y receptoras.
        Optional<Account> emisora = accountRepository.getArsAccountByIdUser(12L);
        Optional<Account> receptora = accountRepository.findByCbu("21234567890123456789");

        double balanceCuentaEmisoraViejo = emisora.get().getBalance();
        double balanceCuentaReceptoraViejo = receptora.get().getBalance();

        //Creamos la transaccion.
        SimpleTransactionDTO simpleTransactionDTO = SimpleTransactionDTO.builder().build();

        simpleTransactionDTO.setAmount(1000.0);
        simpleTransactionDTO.setCBU(receptora.get().getCbu());
        simpleTransactionDTO.setDescription("Transferencia de ARS");

        //Convertir el DTO a JSON
        String transactionJson = objectMapper.writeValueAsString(simpleTransactionDTO);

        //Verificamos que el monto sea mayor al balance y menor al limite de transaccion.
        assertThat(simpleTransactionDTO.getAmount()).isLessThanOrEqualTo(emisora.get().getBalance());
        assertThat(simpleTransactionDTO.getAmount()).isLessThanOrEqualTo(emisora.get().getTransactionLimit());

        //Realizar la solicitud POST para enviar dinero.
        MvcResult result = mockMvc.perform(post("/transactions/sendArs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isCreated())
                .andReturn();

        //Verificamos que los balances se hayan actualizado.
        assertThat(accountRepository.getArsAccountByIdUser(12L).get().getBalance()).
                isLessThan(balanceCuentaEmisoraViejo);
        assertThat(accountRepository.findByCbu("21234567890123456789").get().getBalance()).
                isGreaterThan(balanceCuentaReceptoraViejo);

    }
}
