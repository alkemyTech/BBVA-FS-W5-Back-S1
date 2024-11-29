package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.LoanService;
import com.BBVA.DiMo_S1.D_dtos.loanDTO.CreateLoanDTO;
import com.BBVA.DiMo_S1.D_models.Loan;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanServiceImplementation implements LoanService {

    //1- Simular un préstamo.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public Loan simulateLoan(CreateLoanDTO createLoanDTO) {

        //En caso de que se cumpla con algunas de estas condiciones, se lanza excepción.
        //El préstamo puede ser solo de 12/24/36 meses y su valor debe estar entre 10.000 y 10.000.000.
        if ((createLoanDTO.getPlazo() != 12 && createLoanDTO.getPlazo() != 24 && createLoanDTO.getPlazo() != 36)
                || createLoanDTO.getMonto() < 10000 || createLoanDTO.getMonto() > 10000000) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.LOAN_NO_SE_PUEDE_CREAR);
        }

        //En de que las condiciones se cumplan...
        double interes = 0.05;

        double interesPorCuota = createLoanDTO.getMonto() * interes;

        double cuotaMensual = (createLoanDTO.getMonto() / createLoanDTO.getPlazo()) + interesPorCuota;

        double valorFinal = createLoanDTO.getPlazo() * cuotaMensual;

        Loan prestamo = Loan.builder()
                .monto(createLoanDTO.getMonto())
                .plazo(createLoanDTO.getPlazo())
                .interes(interes)
                .cuotaMensual(BigDecimal.valueOf(cuotaMensual)
                        .setScale(2, RoundingMode.HALF_UP))
                .total(BigDecimal.valueOf(valorFinal)
                        .setScale(2, RoundingMode.HALF_UP))
                .build();

        return prestamo;
    }
    //-----------------------------------------------------------------------------------------------------------
}
