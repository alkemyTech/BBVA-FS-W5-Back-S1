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
    private long id;
    private double amount;
    private String interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
    private boolean settled;

    public FixedTermDepositDTO(FixedTermDeposit fixedTermDeposit) {
        this.id = fixedTermDeposit.getId();
        this.amount = fixedTermDeposit.getAmount();
        this.interest = "2%";
        this.creationDate = fixedTermDeposit.getCreationDate();
        this.closingDate = fixedTermDeposit.getClosingDate();
        this.settled = fixedTermDeposit.isSettled();
    }
}
