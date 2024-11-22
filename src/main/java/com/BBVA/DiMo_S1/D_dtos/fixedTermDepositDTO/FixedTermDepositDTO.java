package com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO;

import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedTermDepositDTO {
    private double amount;
    private double interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;

    public FixedTermDepositDTO(FixedTermDeposit fixedTermDeposit){
        this.amount=fixedTermDeposit.getAmount();
        this.interest=fixedTermDeposit.getInterest();
        this.creationDate=fixedTermDeposit.getCreationDate();
        this.closingDate=fixedTermDeposit.getClosingDate();
    }
}
