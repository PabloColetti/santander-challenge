package com.santander.challenge.ms_accounts.domain.exception;

import java.util.UUID;

/**
 * Exception thrown when an account cannot be found.
 */
public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(UUID id) {
        super("Account with id " + id + " not found");
    }
    
    public AccountNotFoundException(String message) {
        super(message);
    }
}

