package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.FixedTermDepositService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.ShowSimulatedFixedTermDeposit;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.BBVA.DiMo_S1.D_models.Transaction;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Service
public class FixedTermDepositServiceImplementation implements FixedTermDepositService {

    @Autowired
    FixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    TransactionServiceImplementation transactionServiceImplementation;

    //1- Creación de un Plazo Fijo.
    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public ShowSimulatedFixedTermDeposit createFixedTermDeposit(HttpServletRequest request, CreateFixedTermDepositDTO
            createFixedTermDepositDTO) {

        //Inicializamos de forma vacía un Plazo Fijo.
        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder().build();

        //Antes que nada, vamos a validar las 2 primeras condiciones excluyentes en la creacion del Plazo Fijo:
        //El monto debe ser > 1000 y los dias no pueden ser menos de 30.
        boolean diaNoValido = createFixedTermDepositDTO.getCantidadDias() != 30 &&
                createFixedTermDepositDTO.getCantidadDias() != 60 && createFixedTermDepositDTO.getCantidadDias() != 90;

        if (createFixedTermDepositDTO.getAmount() < 1000 || diaNoValido) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_CREACION_PLAZO_FIJO);
        }

        //Si no es una simulación...
        if (!request.getRequestURI().contains("/simulate")) {

            //Vamos a obtener el User que esta en la sesión.
            UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

            //Una vez que obtenemos el User, validamos que el mismo tenga una cuenta en pesos creada.
            Account account = accountRepository.getArsAccountByIdUser(userSecurityDTO.getId())
                    .orElseThrow(() -> new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_CUENTA_PESOS));

            //Si tiene una cuenta, debemos verificar que este en condiciones para poder crear el Plazo Fijo.
            //Si el monto del Plazo Fijo es mayor al balance, lanzamos excepcion.
            if (createFixedTermDepositDTO.getAmount() > account.getBalance()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.SALDO_NO_DISPONIBLE);
            }

            //Como todavia no fue liquidado, lo seteamos en false.
            fixedTermDeposit.setSettled(false);

            //Seteamos la cuenta en el Plazo Fijo y actualizamos el balance del usuario.
            fixedTermDeposit.setAccount(account);
            double balanceActualizado = account.getBalance() - createFixedTermDepositDTO.getAmount();
            account.setBalance(balanceActualizado);
            accountRepository.save(account);

        } else {

            //Si es una simulacion...
            //Seteamos el settled en true y la cuenta en null.
            fixedTermDeposit.setSettled(true);
            fixedTermDeposit.setAccount(null);
        }

        //Seteamos los demas campos. Son iguales tanto para la creacion del Plazo Fijo como para
        //su simulacion
        fixedTermDeposit.setAmount(createFixedTermDepositDTO.getAmount());
        fixedTermDeposit.setInterest(0.02);
        LocalDateTime horaActual = LocalDateTime.now();
        fixedTermDeposit.setCreationDate(horaActual);

        //En caso de que el Plazo Fijo sea de 30 dias, la fecha de vencimiento va a ser la actual + 1 minuto.
        //Si es de 60 +2 y asi sucesivamente...
        if (createFixedTermDepositDTO.getCantidadDias() == 30) {

            fixedTermDeposit.setClosingDate(horaActual.plusMinutes(1l));

        } else if (createFixedTermDepositDTO.getCantidadDias() == 30) {

            fixedTermDeposit.setClosingDate(horaActual.plusMinutes(2l));

        } else {

            fixedTermDeposit.setClosingDate(horaActual.plusMinutes(3l));
        }

        //Inicializamos el DTO.
        ShowSimulatedFixedTermDeposit fixedTermDepositDTO;

        if (!request.getRequestURI().contains("/simulate")) {
            //Guardamos el Plazo Fijo.
            fixedTermDepositRepository.save(fixedTermDeposit);
            fixedTermDepositDTO = new
                    ShowSimulatedFixedTermDeposit(fixedTermDeposit, 0, 0);
        } else {
            double interesGanado = calcularPlazoFijo(fixedTermDeposit) - fixedTermDeposit.getAmount();
            double valorFinal = calcularPlazoFijo(fixedTermDeposit);
            fixedTermDepositDTO = new
                    ShowSimulatedFixedTermDeposit(fixedTermDeposit, interesGanado, valorFinal);
        }

        return fixedTermDepositDTO;
    }
    //----------------------------------------------------------------------------------------------------------------

    //2- Liquidar Plazos Fijos.
    //-----------------------------------------------------------------------------------------------------------------
    @Scheduled(fixedRate = 60000)
    public void liquidarPlazosFijos() {
        //Obtenemos la lista de Plazos Fijos de la BD
        List<FixedTermDeposit> plazosFijos = fixedTermDepositRepository.getFixedTermDepositsWithAccounts();

        for (FixedTermDeposit fixedTermDeposit : plazosFijos) {

            //Si la fecha de cierre es anterior a la actual y todavia no fue liquidado...
            if (fixedTermDeposit.getClosingDate().isBefore(LocalDateTime.now()) &&
                    !fixedTermDeposit.isSettled()) {

                //Lo liquidamos.
                fixedTermDeposit.setSettled(true);

                //Lo guardamos nuevamente en la BD.
                fixedTermDepositRepository.save(fixedTermDeposit);

                //Una vez que liquidamos el Plazo Fijo, debemos actualizar el balance de la cuenta y realizar
                //un deposito.
                //----------------------------------------------------------------------------------------------
                double gananciaPlazoFijo = calcularPlazoFijo(fixedTermDeposit);

                //Genero una Transaction del tipo Deposit para que el User se de cuenta de que se le depositó el
                //Plazo Fijo en su cuenta.
                Transaction depositoPlazoFijo = Transaction.builder().build();
                depositoPlazoFijo.setAmount(gananciaPlazoFijo);
                depositoPlazoFijo.setDescription("Liquidación de Plazo Fijo");
                depositoPlazoFijo.setType(TransactionType.deposit);
                depositoPlazoFijo.setAccount(fixedTermDeposit.getAccount());
                transactionRepository.save(depositoPlazoFijo);

                //Actualizo el balance de la cuenta y la guardo de nuevo en la bd.
                double balanceActualizado = fixedTermDeposit.getAccount().getBalance() + gananciaPlazoFijo;
                fixedTermDeposit.getAccount().setBalance(balanceActualizado);
                accountRepository.save(fixedTermDeposit.getAccount());
            }
            //----------------------------------------------------------------------------------------------
        }
    }
    //-----------------------------------------------------------------------------------------------------------------

    //Calcular Plazo Fijo.
    //-----------------------------------------------------------------------------------------------------------------
    private double calcularPlazoFijo(FixedTermDeposit fixedTermDeposit) {

        //Aca vamos a guardar el valor final obtenido a partir de lo invertido en el Plazo Fijo.
        double valorFinal = 0;

        //Aca vamos a guardar la ganancia obtenida a partir del mismo.
        double ganancia;

        //Verificamos de que tipo de Plazo Fijo se trata averiguando la diferencia en minutos.
        //Si hay un minuto son 30 dias, si hay 2 son 60 dias y asi...
        if (Duration.between(fixedTermDeposit.getCreationDate(), fixedTermDeposit.getClosingDate())
                .toMinutes() == 1) {

            ganancia = (fixedTermDeposit.getAmount() * fixedTermDeposit.getInterest()) * 30;

        } else if (Duration.between(fixedTermDeposit.getCreationDate(), fixedTermDeposit.getClosingDate())
                .toMinutes() == 2) {
            ganancia = (fixedTermDeposit.getAmount() * fixedTermDeposit.getInterest()) * 60;

        } else {

            ganancia = (fixedTermDeposit.getAmount() * fixedTermDeposit.getInterest()) * 90;
        }

        valorFinal = valorFinal + (fixedTermDeposit.getAmount() + ganancia);

        return valorFinal;
    }
    //-----------------------------------------------------------------------------------------------------------------

    //3- Listar Plazos Fijos pertenecientes al usuario.
    //-----------------------------------------------------------------------------------------------------------------
    public Page<FixedTermDepositDTO> getAllFixedTermDepositFromUser(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 10 elementos por página
        Page<FixedTermDeposit> fixedTermDepositPage = fixedTermDepositRepository.
                getFixedTermsDepositsByIdUserPageable(id, pageable);
        return fixedTermDepositPage.map(FixedTermDepositDTO::new);
    }
    //-----------------------------------------------------------------------------------------------------------------

    //- Total de monto invertido, interes ganado y la suma de ambos.
    @Override
    public Map<String, Double> getTotalCalculationsFromUser(Long userId) {

        List<Object[]> results = fixedTermDepositRepository.getTotalInvestedAndInterestByUserId(userId);

        // Inicializar los valores
        Double totalInvertido = 0.0;
        Double totalInteres = 0.0;

        if (!results.isEmpty() && results.get(0) != null) {
            Object[] row = results.get(0);
            totalInvertido = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
            totalInteres = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
        }

        // Calcular el total general
        Double totalGeneral = totalInvertido + totalInteres;

        // Armar el mapa con los resultados
        Map<String, Double> totals = new HashMap<>();
        totals.put("totalInvertido", totalInvertido);
        totals.put("totalInteres", totalInteres);
        totals.put("totalGeneral", totalGeneral);

        return totals;
    }
}
