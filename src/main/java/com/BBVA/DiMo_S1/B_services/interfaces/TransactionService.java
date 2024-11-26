package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;


public interface TransactionService {

    void sendArs(TransactionDTO transactionDTO, String token);

    TransactionDTO sendUsd (HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO);
    Map<String, Object> makePayment(TransactionDTO transactionDTO, String userEmail);
}
