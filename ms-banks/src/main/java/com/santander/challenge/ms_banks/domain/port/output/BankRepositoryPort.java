package com.santander.challenge.ms_banks.domain.port.output;

import com.santander.challenge.ms_banks.domain.model.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de Bank.
 * Abstrae el acceso a datos del dominio.
 */
public interface BankRepositoryPort {
    
    /**
     * Guarda un banco.
     * 
     * @param bank el banco a guardar
     * @return el banco guardado
     */
    Bank save(Bank bank);
    
    /**
     * Busca un banco por ID.
     * 
     * @param id el ID del banco
     * @return Optional con el banco si existe
     */
    Optional<Bank> findById(UUID id);
    
    /**
     * Obtiene todos los bancos con paginación.
     * 
     * @param pageable información de paginación
     * @return página de bancos
     */
    Page<Bank> findAll(Pageable pageable);
    
    /**
     * Busca bancos por país con paginación.
     * 
     * @param country el país
     * @param pageable información de paginación
     * @return página de bancos
     */
    Page<Bank> findByCountry(String country, Pageable pageable);
    
    /**
     * Elimina un banco por ID.
     * 
     * @param id el ID del banco a eliminar
     */
    void deleteById(UUID id);
    
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
    
    /**
     * Verifica si existe un banco con el código dado excluyendo un ID específico.
     * Útil para validaciones en actualizaciones.
     * 
     * @param code el código a verificar
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe otro banco con ese código, false en caso contrario
     */
    boolean existsByCodeAndIdNot(String code, UUID excludeId);
}

