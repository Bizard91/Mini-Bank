package com.example.minibank.exception;

public class AccountClosedException extends RuntimeException {

    public AccountClosedException(String message) {
        super(message);
    }
}
