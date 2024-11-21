package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.TransactionService;
import com.BBVA.DiMo_S1.C_repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class TransactionServiceImplementation implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

}
