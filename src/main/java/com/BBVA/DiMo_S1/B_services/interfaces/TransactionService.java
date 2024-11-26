package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface TransactionService {

    TransactionDTO sendMoney (HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO);
    List<TransactionDTO> getAllTransactionsFromUser(Long id);
}
