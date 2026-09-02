package com.example.minibank.controller;
import com.example.minibank.dto.TransferRequest;

import com.example.minibank.dto.MoneyOperationRequest;
import com.example.minibank.entity.BankTransaction;
import com.example.minibank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public BankTransaction deposit(@Valid @RequestBody MoneyOperationRequest request) {
        return transactionService.deposit(request);
    }

    @PostMapping("/withdraw")
    public BankTransaction withdraw(@Valid @RequestBody MoneyOperationRequest request) {
        return transactionService.withdraw(request);
    }
    @PostMapping("/transfer")
    public BankTransaction transfer(@Valid @RequestBody TransferRequest request) {
        return transactionService.transfer(request);
    }
}