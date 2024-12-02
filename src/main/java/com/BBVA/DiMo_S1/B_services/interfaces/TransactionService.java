package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface TransactionService {

    //1- Realizar una transaccion hacia otra Cuenta.
    TransactionDTO sendMoney(HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO);

    //2- Depositar dinero en una Cuenta.
    TransactionCompletaDTO deposit(HttpServletRequest request, TransactionDepositDTO transactionDepositDTO);

    //3- Obtener detalle de una Transaction buscandola por ID.
    TransactionCompletaDTO transactionDetail(HttpServletRequest request, Long idTransaction);

    //4- Actualizar la descripcion de una Transaction.
    TransactionDTO updateTransactionDescription(Long transactionId, TransactionUpdateDTO transactionUpdateDTO, HttpServletRequest request);

    //5- Listar todas las transacciones de un usuario buscandolo por ID.
    List<TransactionDTO> getAllTransactionsFromUser(Long id);

    //Obtener detalle de una transaction

    Map<String, Object> makePayment(TransactionDTO transactionDTO, HttpServletRequest request);

    //Paginado de transactions
    Page<TransactionDTO> getTransactionsByUser(Long userId, int page);

}
