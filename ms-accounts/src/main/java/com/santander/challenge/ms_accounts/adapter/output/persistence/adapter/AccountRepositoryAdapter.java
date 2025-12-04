package com.santander.challenge.ms_accounts.adapter.output.persistence.adapter;

import com.santander.challenge.ms_accounts.adapter.output.persistence.entity.AccountEntity;
import com.santander.challenge.ms_accounts.adapter.output.persistence.repository.AccountJpaRepository;
import com.santander.challenge.ms_accounts.application.mapper.AccountMapper;
import com.santander.challenge.ms_accounts.domain.model.Account;
import com.santander.challenge.ms_accounts.domain.port.output.AccountRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa AccountRepositoryPort usando JPA.
 */
@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {
    
    private final AccountJpaRepository jpaRepository;
    private final AccountMapper accountMapper;
    
    public AccountRepositoryAdapter(AccountJpaRepository jpaRepository, AccountMapper accountMapper) {
        this.jpaRepository = jpaRepository;
        this.accountMapper = accountMapper;
    }
    
    @Override
    public Account save(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        AccountEntity savedEntity = jpaRepository.save(entity);
        return accountMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(accountMapper::toDomain);
    }
    
    @Override
    public Page<Account> findByBankId(UUID bankId, Pageable pageable) {
        return jpaRepository.findByBankId(bankId, pageable)
                .map(accountMapper::toDomain);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpaRepository.existsByAccountNumber(accountNumber);
    }
    
    @Override
    public Long countByBankId(UUID bankId) {
        return jpaRepository.countByBankId(bankId);
    }
}

