package com.example.minibank.service;

import com.example.minibank.dto.AccountStatusView;
import com.example.minibank.entity.Account;
import com.example.minibank.entity.AccountStatus;
import com.example.minibank.repository.AccountRepository;
import com.example.minibank.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void close_zeroBalance_setsStatusClosed() {
        // Given
        Account account = Account.builder()
                .id(1L)
                .accountNumber("ACC-1")
                .balance(BigDecimal.ZERO)
                .currency("RUB")
                .status(AccountStatus.ACTIVE)
                .build();

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        AccountStatusView view = accountService.close(1L);

        // Then
        assertEquals(AccountStatus.CLOSED, view.status());
        verify(accountRepository).save(account);
    }
}


