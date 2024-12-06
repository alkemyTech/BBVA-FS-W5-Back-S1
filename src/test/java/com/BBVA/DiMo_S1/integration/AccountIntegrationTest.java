package com.BBVA.DiMo_S1.integration;

import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@AutoConfigureMockMvc
public class AccountIntegrationTest {

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
    public void crearCuentaYverificarCreacion() {

        //Inicializamos la cuenta.
        Account account = Account.builder().build();

        //Seteamos los datos.
        account.setBalance(0);
        account.setCurrency(CurrencyType.ARS);
        account.setCbu("44850850485408");
        account.setTransactionLimit(30000);
        account.setUser(userRepository.getById(1L));

        //Persistimos a la account en la BD.
        accountRepository.save(account);

        //Buscamos a la account en la BD para ver si se guardo éxitosamente.
        Optional<Account> savedAccount = accountRepository.findByCbu("44850850485408");
        assertTrue(savedAccount.isPresent());
        assertEquals("44850850485408", savedAccount.get().getCbu());
    }

    @Test
    public void validarEnumCurrency() {
        //Inicializamos la cuenta.
        Account account = Account.builder().build();

        //Seteamos los datos.
        account.setBalance(0);
        account.setCurrency(CurrencyType.ARS);
        account.setCbu("44850850485408");
        account.setTransactionLimit(30000);
        account.setUser(userRepository.getById(1L));

        accountRepository.save(account);

        assertEquals(CurrencyType.ARS, account.getCurrency());
    }

    @Test
    public void timeStampsSeGeneranCorrectamente() {
        //Inicializamos la cuenta.
        Account account = Account.builder().build();

        //Seteamos los datos.
        account.setBalance(0);
        account.setCurrency(CurrencyType.ARS);
        account.setCbu("44850850485408");
        account.setTransactionLimit(30000);
        account.setUser(userRepository.getById(1L));
        accountRepository.save(account);

        assertNotNull(account.getCreationDate());
    }
}

