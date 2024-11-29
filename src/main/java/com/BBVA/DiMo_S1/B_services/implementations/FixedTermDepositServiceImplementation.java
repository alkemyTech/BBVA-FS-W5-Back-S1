package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.FixedTermDepositService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.FixedTermDepositRepository;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.CreateFixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FixedTermDepositServiceImplementation implements FixedTermDepositService {

    @Autowired
    FixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    AccountRepository accountRepository;

    //1- Creación de un Plazo Fijo.
    @Override
    public FixedTermDepositDTO createFixedTermDeposit(HttpServletRequest request, CreateFixedTermDepositDTO createFixedTermDepositDTO) {

        //Inicializamos de forma vacía un Plazo Fijo.
        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder().build();

        //Antes que nada, vamos a validar las 2 primeras condiciones excluyentes en la creacion del Plazo Fijo:
        //El monto debe ser > 1000 y los dias no pueden ser menos de 30.
        boolean diaNoValido = createFixedTermDepositDTO.getCantidadDias() != 30 &&
                createFixedTermDepositDTO.getCantidadDias() != 60 && createFixedTermDepositDTO.getCantidadDias() != 90;

        if (createFixedTermDepositDTO.getAmount() < 1000 || diaNoValido) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_CREACION_PLAZO_FIJO);
        }

        //Si las 2 primeras condiciones se cumplen, vamos a obtener el User que esta en la sesión.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Una vez que obtenemos el User, validamos que el mismo tenga una cuenta en pesos creada.
        Account account = accountRepository.getArsAccountByIdUser(userSecurityDTO.getId())
            .orElseThrow(() -> new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_CUENTA_PESOS));

        //Si tiene una cuenta, debemos verificar que este en condiciones para poder crear el Plazo Fijo.
        //----------------------------------------------------------------------------------------------
        //Si el monto del Plazo Fijo es mayor al balance, lanzamos excepcion.
        if (createFixedTermDepositDTO.getAmount() > account.getBalance()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.SALDO_NO_DISPONIBLE);

        } else {

            //Si el Usuario esta en condiciones de poder realizar el Plazo Fijo (los dias son corectos, el monto
            //> 1000, tiene cuenta en pesos y tiene saldo suficiente).

            //Creamos el Plazo Fijo:
            fixedTermDeposit.setAmount(createFixedTermDepositDTO.getAmount());
            fixedTermDeposit.setInterest(0.2);
            LocalDateTime horaActual = LocalDateTime.now();
            fixedTermDeposit.setCreationDate(horaActual);
            fixedTermDeposit.setSettled(false); //Como todavia no fue liquidado, lo seteamos en false.

            //En caso de que el Plazo Fijo sea de 30 dias, la fecha de vencimiento va a ser la actual + 1 minuto.
            //Si es de 60 +2 y asi sucesivamente...
            if (createFixedTermDepositDTO.getCantidadDias() == 30) {

                fixedTermDeposit.setClosingDate(horaActual.plusMinutes(1l));

            } else if (createFixedTermDepositDTO.getCantidadDias() == 30) {

                fixedTermDeposit.setClosingDate(horaActual.plusMinutes(2l));

            } else {

                fixedTermDeposit.setClosingDate(horaActual.plusMinutes(3l));
            }

            fixedTermDeposit.setAccount(account);

            //Una vez que seteamos todos los campos del Plazo Fijo, le sacamos el dinero al
            //Usuario de su Account ya que el mismo fue invertido en la creacion del Plazo.
            double balanceActualizado = account.getBalance() - createFixedTermDepositDTO.getAmount();
            account.setBalance(balanceActualizado);
            accountRepository.save(account);
        }
        //----------------------------------------------------------------------------------------------

        //Guardamos el Plazo Fijo.
        FixedTermDeposit fixedTermDepositGuardado = fixedTermDepositRepository.save(fixedTermDeposit);

        //Cargamos el DTO para poder mostrarlo.
        FixedTermDepositDTO fixedTermDepositDTO = new FixedTermDepositDTO(fixedTermDepositGuardado);

        return fixedTermDepositDTO;
    }

    @Override
    public double calcularPlazoFijo (List<FixedTermDeposit> plazosFijos) {

        //Aca vamos a guardar el valor final obtenido a partir de lo invertido en el Plazo Fijo.
        double valorFinal = 0;

        //Aca vamos a guardar la ganancia obtenida a partir del mismo.
        double ganancia = 0;

        //Vamos a recorrer la lista de Plazos Fijos y verificar si hay alguno que ya finalizó.
        for (FixedTermDeposit fixedTermDeposit : plazosFijos) {

            //Si la fecha de cierre es anterior a la actual y todavia no fue liquidado...
            if (fixedTermDeposit.getClosingDate().isBefore(LocalDateTime.now()) &&
                    !fixedTermDeposit.isSettled()) {

                //Verificamos de que tipo de Plazo Fijo se trata averiguando la diferencia en minutos.
                //Si hay un minuto son 30 dias, si hay 2 son 60 dias y asi...
                if (Duration.between(fixedTermDeposit.getCreationDate(), fixedTermDeposit.getClosingDate())
                        .toMinutes() == 1) {

                    ganancia = (fixedTermDeposit.getAmount() * 0.002) * 30;

                    valorFinal = valorFinal + (fixedTermDeposit.getAmount() + ganancia);


                } else if (Duration.between(fixedTermDeposit.getCreationDate(), fixedTermDeposit.getClosingDate())
                        .toMinutes() == 2) {

                    ganancia = (fixedTermDeposit.getAmount() * 0.002) * 60 ;

                    valorFinal = valorFinal + (fixedTermDeposit.getAmount() + ganancia);

                } else {

                    ganancia = (fixedTermDeposit.getAmount() * 0.002) * 90 ;

                    valorFinal = valorFinal + (fixedTermDeposit.getAmount() + ganancia);
                }

                //Antes de continuar recorriendo la lista, seteamos el settled en true para establecer que
                //el Plazo Fijo ya fue liquidado. Lo guardamos nuevamente en la BD.
                fixedTermDeposit.setSettled(true);
                fixedTermDepositRepository.save(fixedTermDeposit);
            }
        }

        return valorFinal;
    }
}
