package com.santander.challenge.ms_accounts.domain.port.input;

import com.santander.challenge.ms_accounts.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de entrada para los casos de uso de Account.
 * Define las operaciones que el dominio puede realizar.
 */
public interface AccountServicePort {
    
    /**
     * Crea una nueva cuenta.
     * 
     * @param account la cuenta a crear
     * @return la cuenta creada
     */
    Account createAccount(Account account);
    
    /**
     * Obtiene una cuenta por su ID, validando que pertenezca al bankId especificado.
     * 
     * @param id el ID de la cuenta
     * @param bankId el ID del banco (para aislamiento de datos)
     * @return la cuenta encontrada
     * @throws com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException si no se encuentra
     * @throws com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException si no pertenece al banco
     */
    Account getAccountById(UUID id, UUID bankId);
    
    /**
     * Lista cuentas de un banco específico con paginación.
     * 
     * @param bankId el ID del banco (requerido para aislamiento)
     * @param pageable información de paginación
     * @return página de cuentas
     */
    Page<Account> getAccountsByBankId(UUID bankId, Pageable pageable);
    
    /**
     * Actualiza una cuenta existente.
     * 
     * @param id el ID de la cuenta a actualizar
     * @param bankId el ID del banco (para validación)
     * @param account los datos actualizados
     * @return la cuenta actualizada
     * @throws com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException si no se encuentra
     * @throws com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException si no pertenece al banco
     */
    Account updateAccount(UUID id, UUID bankId, Account account);
    
    /**
     * Elimina una cuenta.
     * 
     * @param id el ID de la cuenta a eliminar
     * @param bankId el ID del banco (para validación)
     * @throws com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException si no se encuentra
     * @throws com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException si no pertenece al banco
     */
    void deleteAccount(UUID id, UUID bankId);
    
    /**
     * Cuenta las cuentas asociadas a un banco.
     * 
     * @param bankId el ID del banco
     * @return el número de cuentas
     */
    Long countByBankId(UUID bankId);
    
    /**
     * Verifica si existe una cuenta con el número dado.
     * 
     * @param accountNumber el número de cuenta a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByAccountNumber(String accountNumber);
}

