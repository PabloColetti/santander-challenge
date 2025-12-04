package com.santander.challenge.ms_banks.domain.exception;

/**
 * Excepción lanzada cuando hay errores de validación en los datos del banco.
 */
public class BankValidationException extends RuntimeException {
    
    public BankValidationException(String message) {
        super(message);
    }
    
    public BankValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

