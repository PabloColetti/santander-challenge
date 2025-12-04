package com.santander.challenge.ms_banks.domain.port.input;

import com.santander.challenge.ms_banks.domain.model.Bank;

import java.util.UUID;

/**
 * Puerto de entrada para el servicio interno de Bank.
 * Permite realizar llamadas internas al microservicio.
 */
public interface BankInternalServicePort {
    
    /**
     * Obtiene un banco por ID mediante llamada interna (llamada a sí mismo).
     * Este método demuestra la capacidad del microservicio de consumir sus propios endpoints.
     * 
     * @param id el ID del banco
     * @return el banco encontrado
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException si no se encuentra
     */
    Bank getBankByIdInternal(UUID id);
}

