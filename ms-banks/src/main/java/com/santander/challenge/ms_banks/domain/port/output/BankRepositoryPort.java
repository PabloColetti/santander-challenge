package com.santander.challenge.ms_banks.domain.port.output;

import com.santander.challenge.ms_banks.domain.model.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Bank persistence.
 * Abstracts domain access to data storage.
 */
public interface BankRepositoryPort {
    
    /**
     * Saves a bank.
     *
     * @param bank bank to persist
     * @return persisted bank
     */
    Bank save(Bank bank);
    
    /**
     * Finds a bank by its identifier.
     *
     * @param id bank identifier
     * @return optional containing the bank if present
     */
    Optional<Bank> findById(UUID id);
    
    /**
     * Retrieves paginated banks.
     *
     * @param pageable pagination metadata
     * @return page of banks
     */
    Page<Bank> findAll(Pageable pageable);
    
    /**
     * Retrieves paginated banks filtered by country.
     *
     * @param country country filter
     * @param pageable pagination metadata
     * @return page of banks
     */
    Page<Bank> findByCountry(String country, Pageable pageable);
    
    /**
     * Deletes a bank by its identifier.
     *
     * @param id bank identifier
     */
    void deleteById(UUID id);
    
    /**
     * Checks whether a bank exists with the given code.
     *
     * @param code code to validate
     * @return true when a bank exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Finds a bank by its code.
     *
     * @param code bank code
     * @return optional containing the bank if present
     */
    Optional<Bank> findByCode(String code);
    
    /**
     * Checks if a bank exists with the given code excluding a specific identifier.
     * Useful during update validations.
     *
     * @param code code to validate
     * @param excludeId identifier to exclude from the lookup
     * @return true when another bank with the same code exists, false otherwise
     */
    boolean existsByCodeAndIdNot(String code, UUID excludeId);
}

