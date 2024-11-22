package com.BBVA.DiMo_S1.D_dtos.TransactionDTO;

import com.BBVA.DiMo_S1.D_models.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;

    private Double amount;

    private TransactionType type;

    private String description;

    private LocalDateTime transactionDate;

    private Long accountId; // Clave foránea
}
