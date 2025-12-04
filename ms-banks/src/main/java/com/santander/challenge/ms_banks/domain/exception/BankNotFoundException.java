package com.santander.challenge.ms_banks.domain.exception;

import java.util.UUID;

/**
 * Exception thrown when a bank cannot be found.
 */
public class BankNotFoundException extends RuntimeException {
    
    public BankNotFoundException(UUID id) {
        super("Bank with id " + id + " not found");
    }
    
    public BankNotFoundException(String message) {
        super(message);
    }
}

