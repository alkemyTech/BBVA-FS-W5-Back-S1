package com.BBVA.DiMo_S1.D_dtos.accountDTO;

import com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO.FixedTermDepositDTO;
import com.BBVA.DiMo_S1.D_dtos.transactionDTO.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDto {
    private double balanceArs=0;
    private double balanceUsd=0;
    private List<TransactionDTO> transactionDTOList;
    private  List<FixedTermDepositDTO> fixedTermDepositDTOList;
}
