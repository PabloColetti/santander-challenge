package com.santander.challenge.ms_banks.domain.port.input;

import com.santander.challenge.ms_banks.domain.model.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de entrada para los casos de uso de Bank.
 * Define las operaciones que el dominio puede realizar.
 */
public interface BankServicePort {
    
    /**
     * Crea un nuevo banco.
     * 
     * @param bank el banco a crear
     * @return el banco creado
     */
    Bank createBank(Bank bank);
    
    /**
     * Obtiene un banco por su ID.
     * 
     * @param id el ID del banco
     * @return el banco encontrado
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException si no se encuentra
     */
    Bank getBankById(UUID id);
    
    /**
     * Lista todos los bancos con paginación.
     * 
     * @param pageable información de paginación
     * @return página de bancos
     */
    Page<Bank> getAllBanks(Pageable pageable);
    
    /**
     * Lista bancos filtrados por país.
     * 
     * @param country el país para filtrar
     * @param pageable información de paginación
     * @return página de bancos
     */
    Page<Bank> getBanksByCountry(String country, Pageable pageable);
    
    /**
     * Actualiza un banco existente.
     * 
     * @param id el ID del banco a actualizar
     * @param bank los datos actualizados
     * @return el banco actualizado
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException si no se encuentra
     */
    Bank updateBank(UUID id, Bank bank);
    
    /**
     * Elimina un banco.
     * 
     * @param id el ID del banco a eliminar
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException si no se encuentra
     * @throws com.santander.challenge.ms_banks.domain.exception.BankHasAccountsException si tiene cuentas asociadas
     */
    void deleteBank(UUID id);
    
    /**
     * Verifica si existe un banco con el código dado.
     * 
     * @param code el código a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Busca un banco por su código.
     * 
     * @param code el código del banco
     * @return Optional con el banco si existe
     */
    Optional<Bank> findByCode(String code);
}

