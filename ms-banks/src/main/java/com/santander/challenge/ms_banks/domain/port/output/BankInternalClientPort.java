package com.santander.challenge.ms_banks.domain.port.output;

import com.santander.challenge.ms_banks.domain.model.Bank;

import java.util.UUID;

/**
 * Puerto de salida para llamadas internas al microservicio.
 * Abstrae el cliente HTTP interno (Feign).
 */
public interface BankInternalClientPort {
    
    /**
     * Obtiene un banco por ID mediante llamada HTTP interna.
     * 
     * @param id el ID del banco
     * @return el banco encontrado
     */
    Bank getBankById(UUID id);
}

