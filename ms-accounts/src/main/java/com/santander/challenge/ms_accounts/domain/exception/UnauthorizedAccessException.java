package com.santander.challenge.ms_accounts.domain.exception;

/**
 * Excepción lanzada cuando un banco intenta acceder a cuentas que no le pertenecen.
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

