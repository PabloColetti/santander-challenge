package com.santander.challenge.ms_accounts.domain.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando un banco no se encuentra.
 * Usada cuando se intenta crear/actualizar una cuenta con un bankId que no existe.
 */
public class BankNotFoundException extends RuntimeException {
    
    public BankNotFoundException(UUID id) {
        super("Bank with id " + id + " not found");
    }
    
    public BankNotFoundException(String message) {
        super(message);
    }
}

