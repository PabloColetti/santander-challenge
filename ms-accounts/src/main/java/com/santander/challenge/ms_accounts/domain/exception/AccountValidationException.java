package com.santander.challenge.ms_accounts.domain.exception;

/**
 * Exception thrown when account data fails validation.
 */
public class AccountValidationException extends RuntimeException {
    
    public AccountValidationException(String message) {
        super(message);
    }
    
    public AccountValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

