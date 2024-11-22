package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.TransactionDTO.TransactionDTO;


public interface TransactionService {

    void sendArs(TransactionDTO transactionDTO, String token);
}
