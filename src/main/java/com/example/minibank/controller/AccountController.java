package com.example.minibank.controller;

import com.example.minibank.dto.CreateAccountRequest;
import com.example.minibank.entity.Account;
import com.example.minibank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Account createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<Account> getAccountsByClientId(@PathVariable Long clientId) {
        return accountService.getAccountsByClientId(clientId);
    }
}
