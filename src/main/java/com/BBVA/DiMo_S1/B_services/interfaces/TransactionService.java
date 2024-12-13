package com.BBVA.DiMo_S1.B_services.interfaces;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionUpdateDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;


public interface TransactionService {

    //1- Realizar una transaccion hacia otra Cuenta.
    TransactionDTO sendMoney(HttpServletRequest request, SimpleTransactionDTO simpleTransactionDTO);

    //2- Depositar dinero en una Cuenta.
    TransactionDTO deposit(HttpServletRequest request, TransactionDepositDTO transactionDepositDTO);

    //3- Obtener detalle de una Transaction buscandola por ID.
    TransactionDTO transactionDetail(HttpServletRequest request, Long idTransaction);

    //4- Actualizar la descripcion de una Transaction.
    TransactionDTO updateTransactionDescription(Long transactionId, TransactionUpdateDTO transactionUpdateDTO, HttpServletRequest request);

    //5- Listar todas las transacciones de un usuario buscandolo por ID.
    Page<TransactionDTO> getAllTransactionsFromUser(Long id, int page, int size);

    //Obtener detalle de una transaction
    TransactionDTO makePayment(TransactionDepositDTO transactionDTO, HttpServletRequest request);

}
