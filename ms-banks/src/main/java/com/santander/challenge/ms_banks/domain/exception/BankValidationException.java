package com.santander.challenge.ms_banks.domain.exception;

/**
 * Exception thrown when bank data fails validation.
 */
public class BankValidationException extends RuntimeException {
    
    public BankValidationException(String message) {
        super(message);
    }
    
    public BankValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

