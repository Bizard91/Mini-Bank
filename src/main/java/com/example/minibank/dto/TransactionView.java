package com.example.minibank.dto;

import com.example.minibank.entity.BankTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionView(
        Long id,
        String type,
        BigDecimal amount,
        Long fromAccountId,
        Long toAccountId,
        String description,
        LocalDateTime createdAt
) {
    public static TransactionView from (BankTransaction tx){
    return new TransactionView(
            tx.getId(),
            tx.getType().name(),
            tx.getAmount(),
            tx.getFromAccount() != null ? tx.getFromAccount().getId() : null,
            tx.getToAccount() != null ? tx.getToAccount().getId() : null,
            tx.getDescription(),
            tx.getCreatedAt()
    );

    }

}
