package com.example.minibank.dto;

import com.example.minibank.entity.AccountStatus;

import java.math.BigDecimal;

public record AccountStatusView(
        Long id,
        AccountStatus status,
        BigDecimal balance
) {

}
