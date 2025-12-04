package com.santander.challenge.ms_accounts.domain.port.output;

import com.santander.challenge.ms_accounts.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de Account.
 * Abstrae el acceso a datos del dominio.
 */
public interface AccountRepositoryPort {
    
    /**
     * Guarda una cuenta.
     * 
     * @param account la cuenta a guardar
     * @return la cuenta guardada
     */
    Account save(Account account);
    
    /**
     * Busca una cuenta por ID.
     * 
     * @param id el ID de la cuenta
     * @return Optional con la cuenta si existe
     */
    Optional<Account> findById(UUID id);
    
    /**
     * Busca cuentas por bankId con paginación.
     * 
     * @param bankId el ID del banco
     * @param pageable información de paginación
     * @return página de cuentas
     */
    Page<Account> findByBankId(UUID bankId, Pageable pageable);
    
    /**
     * Elimina una cuenta por ID.
     * 
     * @param id el ID de la cuenta a eliminar
     */
    void deleteById(UUID id);
    
    /**
     * Verifica si existe una cuenta con el número dado.
     * 
     * @param accountNumber el número de cuenta a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Cuenta las cuentas asociadas a un banco.
     * 
     * @param bankId el ID del banco
     * @return el número de cuentas
     */
    Long countByBankId(UUID bankId);
}

