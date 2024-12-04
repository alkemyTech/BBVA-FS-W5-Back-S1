package com.BBVA.DiMo_S1.D_dtos.fixedTermDepositDTO;

import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShowSimulatedFixedTermDeposit {
    private double amount;
    private String interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
    private boolean settled;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double interestEarned;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double finalAmount;

    public ShowSimulatedFixedTermDeposit(FixedTermDeposit fixedTermDeposit, double interestEarned,
                                         double finalAmount) {
        this.amount = fixedTermDeposit.getAmount();
        this.interest = "2%";
        this.creationDate = fixedTermDeposit.getCreationDate();
        this.closingDate = fixedTermDeposit.getClosingDate();
        this.settled = fixedTermDeposit.isSettled();
        this.interestEarned = interestEarned;
        this.finalAmount = finalAmount;
    }
}