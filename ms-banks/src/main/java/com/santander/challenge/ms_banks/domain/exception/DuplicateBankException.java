package com.santander.challenge.ms_banks.domain.exception;

/**
 * Exception thrown when a bank is created or updated with an existing code.
 */
public class DuplicateBankException extends RuntimeException {
    
    public DuplicateBankException(String code) {
        super("Bank with code " + code + " already exists");
    }
    
    public DuplicateBankException(String message, Throwable cause) {
        super(message, cause);
    }
}

