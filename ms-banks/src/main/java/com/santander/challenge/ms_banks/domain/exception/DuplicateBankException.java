package com.santander.challenge.ms_banks.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear o actualizar un banco con un código que ya existe.
 */
public class DuplicateBankException extends RuntimeException {
    
    public DuplicateBankException(String code) {
        super("Bank with code " + code + " already exists");
    }
    
    public DuplicateBankException(String message, Throwable cause) {
        super(message, cause);
    }
}

