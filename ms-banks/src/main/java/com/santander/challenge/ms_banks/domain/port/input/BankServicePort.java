package com.santander.challenge.ms_banks.domain.port.input;

import com.santander.challenge.ms_banks.domain.model.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Input port for Bank use cases defining the operations available to the domain.
 */
public interface BankServicePort {
    
    /**
     * Creates a new bank.
     *
     * @param bank bank to create
     * @return persisted bank
     */
    Bank createBank(Bank bank);
    
    /**
     * Retrieves a bank by its identifier.
     *
     * @param id bank identifier
     * @return bank found
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException when not found
     */
    Bank getBankById(UUID id);
    
    /**
     * Lists banks with pagination.
     *
     * @param pageable pagination metadata
     * @return page of banks
     */
    Page<Bank> getAllBanks(Pageable pageable);
    
    /**
     * Lists banks filtered by country.
     *
     * @param country country filter
     * @param pageable pagination metadata
     * @return page of banks
     */
    Page<Bank> getBanksByCountry(String country, Pageable pageable);
    
    /**
     * Updates an existing bank.
     *
     * @param id bank identifier
     * @param bank updated data
     * @return updated bank
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException when not found
     */
    Bank updateBank(UUID id, Bank bank);
    
    /**
     * Deletes a bank.
     *
     * @param id bank identifier
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException when not found
     * @throws com.santander.challenge.ms_banks.domain.exception.BankHasAccountsException when accounts exist
     */
    void deleteBank(UUID id);
    
    /**
     * Checks whether a bank exists with the given code.
     *
     * @param code code to validate
     * @return true if a bank exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Finds a bank by its code.
     *
     * @param code bank code
     * @return optional containing the bank if present
     */
    Optional<Bank> findByCode(String code);
}

