package com.santander.challenge.ms_accounts.domain.port.output;

import com.santander.challenge.ms_accounts.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Account persistence that abstracts domain access to data storage.
 */
public interface AccountRepositoryPort {
    
    /**
     * Saves an account.
     *
     * @param account account to persist
     * @return persisted account
     */
    Account save(Account account);
    
    /**
     * Finds an account by its identifier.
     *
     * @param id account identifier
     * @return optional containing the account when present
     */
    Optional<Account> findById(UUID id);
    
    /**
     * Retrieves accounts by bank identifier with pagination.
     *
     * @param bankId bank identifier
     * @param pageable pagination metadata
     * @return page of accounts
     */
    Page<Account> findByBankId(UUID bankId, Pageable pageable);
    
    /**
     * Deletes an account by its identifier.
     *
     * @param id account identifier
     */
    void deleteById(UUID id);
    
    /**
     * Checks whether an account exists with the given account number.
     *
     * @param accountNumber account number to validate
     * @return true when an account exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Counts the accounts associated with a bank.
     *
     * @param bankId bank identifier
     * @return number of accounts
     */
    Long countByBankId(UUID bankId);
}

