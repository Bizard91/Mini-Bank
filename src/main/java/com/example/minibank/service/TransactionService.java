package com.example.minibank.service;
import com.example.minibank.dto.TransactionView;
import com.example.minibank.dto.TransferRequest;
import org.springframework.transaction.annotation.Transactional;
import com.example.minibank.dto.MoneyOperationRequest;
import com.example.minibank.entity.Account;
import com.example.minibank.entity.BankTransaction;
import com.example.minibank.entity.TransactionType;
import com.example.minibank.repository.AccountRepository;
import com.example.minibank.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.minibank.exception.NotEnoughMoneyException;
import com.example.minibank.exception.ResourceNotFoundException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    public BankTransaction deposit(MoneyOperationRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() ->  new ResourceNotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        BankTransaction transaction = BankTransaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .toAccount(account)
                .description("Deposit to account")
                .build();

        return transactionRepository.save(transaction);
    }

    public BankTransaction withdraw(MoneyOperationRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new NotEnoughMoneyException("Not enough money");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        BankTransaction transaction = BankTransaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .fromAccount(account)
                .description("Withdraw from account")
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public BankTransaction transfer(TransferRequest request) {

        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new NotEnoughMoneyException("Not enough money");
        }

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(request.getAmount())
        );

        toAccount.setBalance(
                toAccount.getBalance().add(request.getAmount())
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        BankTransaction transaction = BankTransaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .description("Transfer between accounts")
                .build();

        return transactionRepository.save(transaction);
    }
    @Transactional
    public List<TransactionView> getHistory(Long accountId) {
      accountRepository.findById(accountId)
              .orElseThrow(() -> new ResourceNotFoundException("Account not found"+accountId));
        List<BankTransaction> transactions = transactionRepository
                .findAllByFromAccountIdOrToAccountId(accountId, accountId);

        return transactions.stream()
                .map(TransactionView::from)
                .toList();
    }

    }

