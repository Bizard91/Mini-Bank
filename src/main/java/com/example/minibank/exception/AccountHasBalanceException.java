package com.example.minibank.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class AccountHasBalanceException extends RuntimeException {

    public AccountHasBalanceException(String message) {
        super(message);
    }
}

