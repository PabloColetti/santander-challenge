package com.santander.challenge.ms_banks.domain.port.output;

import java.util.UUID;

/**
 * Puerto de salida para contar cuentas de un banco.
 * Abstrae la comunicación con ms-accounts.
 */
public interface AccountCountPort {
    
    /**
     * Cuenta las cuentas asociadas a un banco.
     * 
     * @param bankId el ID del banco
     * @return el número de cuentas
     */
    Long countByBankId(UUID bankId);
}

