package com.example.minibank.service;

import com.example.minibank.dto.CreateAccountRequest;
import com.example.minibank.entity.Account;
import com.example.minibank.entity.AccountStatus;
import com.example.minibank.entity.Client;
import com.example.minibank.repository.AccountRepository;
import com.example.minibank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.minibank.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public Account createAccount(CreateAccountRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        // Попробовал написать методом builder(), который сгенерировал
        // Project Lombok через @Builder.
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .currency(request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .client(client)
                .build();

        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public List<Account> getAccountsByClientId(Long clientId) {
        return accountRepository.findAllByClientId(clientId);
    }

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}