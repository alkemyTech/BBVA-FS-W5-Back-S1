package com.BBVA.DiMo_S1.D_dtos.transactionDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionUpdateDTO {
    private String description;
}
