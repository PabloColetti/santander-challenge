package com.santander.challenge.ms_accounts.domain.port.input;

import com.santander.challenge.ms_accounts.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Input port for Account use cases defining the domain operations.
 */
public interface AccountServicePort {
    
    /**
     * Creates a new account.
     *
     * @param account account to create
     * @return persisted account
     */
    Account createAccount(Account account);
    
    /**
     * Retrieves an account by its identifier while validating that it belongs to the provided bankId.
     *
     * @param id account identifier
     * @param bankId bank identifier used for isolation
     * @return account found
     * @throws com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException when not found
     * @throws com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException when it does not belong to the bank
     */
    Account getAccountById(UUID id, UUID bankId);
    
    /**
     * Lists accounts for a specific bank with pagination.
     *
     * @param bankId bank identifier (required for isolation)
     * @param pageable pagination metadata
     * @return page of accounts
     */
    Page<Account> getAccountsByBankId(UUID bankId, Pageable pageable);
    
    /**
     * Updates an existing account.
     *
     * @param id account identifier
     * @param bankId bank identifier used for validation
     * @param account updated data
     * @return updated account
     * @throws com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException when not found
     * @throws com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException when it does not belong to the bank
     */
    Account updateAccount(UUID id, UUID bankId, Account account);
    
    /**
     * Deletes an account.
     *
     * @param id account identifier
     * @param bankId bank identifier used for validation
     * @throws com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException when not found
     * @throws com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException when it does not belong to the bank
     */
    void deleteAccount(UUID id, UUID bankId);
    
    /**
     * Counts accounts associated with a bank.
     *
     * @param bankId bank identifier
     * @return number of accounts
     */
    Long countByBankId(UUID bankId);
    
    /**
     * Checks whether an account exists with the given account number.
     *
     * @param accountNumber account number to validate
     * @return true when an account exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
}

