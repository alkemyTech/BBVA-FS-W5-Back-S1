package com.BBVA.DiMo_S1.mocks;

import com.BBVA.DiMo_S1.D_dtos.accountDTO.BalanceDto;
import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionCompletaDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.Enums.TransactionType;
import lombok.Builder;
import org.apache.coyote.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder

public class Mocks {

    public static SimpleTransactionDTO simpleTransactionDTO(){
        SimpleTransactionDTO simpleTransactionDTO = SimpleTransactionDTO.builder().build();

        simpleTransactionDTO.setAmount(1000d);
        simpleTransactionDTO.setDescription("Pago");
        simpleTransactionDTO.setCBU("87546465433788799977");

        return simpleTransactionDTO;
    }

    public static BalanceDto balanceDto(){
        List<TransactionDTO> listTransaction = new ArrayList<>();
        List<FixedTermDepositDTO> listFixed = new ArrayList<>();


        BalanceDto balanceDto = BalanceDto.builder().build();

        balanceDto.setBalanceUsd(200);
        balanceDto.setBalanceArs(10000);
        balanceDto.setTransactions(listTransaction);
        balanceDto.setFixedTermDeposits(listFixed);

        return balanceDto;
    }

    public static TransactionDepositDTO transactionDepositDTO(){

        TransactionDepositDTO transactionDTO = TransactionDepositDTO.builder().build();

        transactionDTO.setAmount(300d);
        transactionDTO.setDescription("Descripcion");
        transactionDTO.setCurrencyType(CurrencyType.ARS);

        return transactionDTO;

    }

    public static TransactionDTO transactionCompletaDTO() {


        TransactionDTO transactionCompletaDTO= TransactionDTO.builder().build();

        transactionCompletaDTO.setAmount(200d);
        transactionCompletaDTO.setCurrencyType(CurrencyType.ARS);
        transactionCompletaDTO.setType(TransactionType.payment);
        transactionCompletaDTO.setDescription("PAGO");
        transactionCompletaDTO.setTransactionDate(LocalDateTime.now());
        transactionCompletaDTO.setCuenta("Cuenta");
        transactionCompletaDTO.setTitular("Cuenta titular");
        transactionCompletaDTO.setCuentaDestino("Cuenta Destino");

        return transactionCompletaDTO;
    }

    public static UserSecurityDTO userSecurityDTO(){
        UserSecurityDTO userSecurityDTO = UserSecurityDTO.builder().build();




        userSecurityDTO.setId(1L);
        userSecurityDTO.setEmail("mailprueba@gmail.com");
        userSecurityDTO.setRole("USER");
        userSecurityDTO.setCreatedAt(LocalDateTime.now());
        userSecurityDTO.setExpirationDate(LocalDateTime.now());

        return userSecurityDTO;

    }



}
