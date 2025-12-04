package com.santander.challenge.ms_accounts.domain.exception;

import java.util.UUID;

/**
 * Exception thrown when a bank cannot be found, typically when creating or updating
 * an account with a non-existent bankId.
 */
public class BankNotFoundException extends RuntimeException {
    
    public BankNotFoundException(UUID id) {
        super("Bank with id " + id + " not found");
    }
    
    public BankNotFoundException(String message) {
        super(message);
    }
}

