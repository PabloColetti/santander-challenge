package com.santander.challenge.ms_accounts.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear o actualizar una cuenta con un número que ya existe.
 */
public class DuplicateAccountException extends RuntimeException {
    
    public DuplicateAccountException(String accountNumber) {
        super("Account with number " + accountNumber + " already exists");
    }
    
    public DuplicateAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}

