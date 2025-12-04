package com.santander.challenge.ms_accounts.domain.exception;

/**
 * Excepción lanzada cuando hay errores de validación en los datos de la cuenta.
 */
public class AccountValidationException extends RuntimeException {
    
    public AccountValidationException(String message) {
        super(message);
    }
    
    public AccountValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

