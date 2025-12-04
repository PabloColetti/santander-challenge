package com.santander.challenge.ms_accounts.adapter.output.persistence.repository;

import com.santander.challenge.ms_accounts.adapter.output.persistence.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para AccountEntity.
 */
@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
    
    boolean existsByAccountNumber(String accountNumber);
    
    Page<AccountEntity> findByBankId(UUID bankId, Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.bankId = :bankId")
    Long countByBankId(@Param("bankId") UUID bankId);
}

