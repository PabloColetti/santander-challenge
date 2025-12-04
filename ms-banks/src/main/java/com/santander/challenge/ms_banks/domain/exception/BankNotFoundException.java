package com.santander.challenge.ms_banks.domain.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando un banco no se encuentra.
 */
public class BankNotFoundException extends RuntimeException {
    
    public BankNotFoundException(UUID id) {
        super("Bank with id " + id + " not found");
    }
    
    public BankNotFoundException(String message) {
        super(message);
    }
}

