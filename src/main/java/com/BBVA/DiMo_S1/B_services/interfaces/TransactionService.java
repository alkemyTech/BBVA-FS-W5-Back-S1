package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionCompletaDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import jakarta.servlet.http.HttpServletRequest;


public interface TransactionService {

    TransactionDTO sendMoney (HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO);

    // Realizar un deposito

    TransactionCompletaDTO deposit(HttpServletRequest request, TransactionDepositDTO transactionDepositDTO);


}
