package com.BBVA.DiMo_S1.Mocks;

import com.BBVA.DiMo_S1.D_dtos.transactionDTO.SimpleTransactionDTO;

public class MocksTransaction {
    /*private Double amount;
    private String description;
    private String CBU;
    */
    public static SimpleTransactionDTO mockSimpleTransactionDTO(){
        SimpleTransactionDTO simpleTransactionDTO = SimpleTransactionDTO.builder().build();
        simpleTransactionDTO.setAmount(10000d);
        simpleTransactionDTO.setCBU("52059060623642715578");
        simpleTransactionDTO.setDescription("Pago servicio");
        return simpleTransactionDTO;
    }
}
