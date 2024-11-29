package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionCompletaDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface TransactionService {

    TransactionDTO sendMoney (HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO);


    // Realizar un deposito

    TransactionCompletaDTO deposit(HttpServletRequest request, TransactionDepositDTO transactionDepositDTO);


    List<TransactionDTO> getAllTransactionsFromUser(Long id);

    //Obtener detalle de una transaction

    TransactionCompletaDTO transactionDetail(HttpServletRequest request, Long idTransaction);

    Map<String, Object> makePayment(TransactionDTO transactionDTO, HttpServletRequest request);

    TransactionDTO updateTransactionDescription(Long transactionId, String newDescription, HttpServletRequest request);

    //Paginado de transactions
    Page<TransactionDTO> getTransactionsByUser(Long userId, int page);

}
