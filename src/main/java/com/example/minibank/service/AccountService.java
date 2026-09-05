package com.example.minibank.service;

import com.example.minibank.dto.AccountStatusView;
import com.example.minibank.dto.CreateAccountRequest;
import com.example.minibank.entity.Account;
import com.example.minibank.entity.AccountStatus;
import com.example.minibank.entity.Client;
import com.example.minibank.exception.AccountClosedException;
import com.example.minibank.exception.AccountHasBalanceException;
import com.example.minibank.repository.AccountRepository;
import com.example.minibank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.minibank.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public AccountStatusView close(Long id) {
        Account account = accountRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found" + id));
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new AccountClosedException("Account is already closed: " + id);
        }
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountHasBalanceException(
                    "Cannot close account with positive balance: " + account.getBalance()
            );
        }
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

        return new AccountStatusView(
                account.getId(),
                account.getStatus(),
                account.getBalance()
        );

    }
}