package com.santander.challenge.ms_accounts.domain.port.output;

import java.util.UUID;

/**
 * Puerto de salida para validar la existencia de un banco.
 * Abstrae la comunicación con ms-banks.
 */
public interface BankValidationPort {
    
    /**
     * Verifica si un banco existe.
     * 
     * @param bankId el ID del banco
     * @return true si existe, false en caso contrario
     */
    boolean existsById(UUID bankId);
}

