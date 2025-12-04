package com.santander.challenge.ms_accounts.domain.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando una cuenta no se encuentra.
 */
public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(UUID id) {
        super("Account with id " + id + " not found");
    }
    
    public AccountNotFoundException(String message) {
        super(message);
    }
}

