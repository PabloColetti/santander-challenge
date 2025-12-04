package com.santander.challenge.ms_banks.adapter.output.persistence.repository;

import com.santander.challenge.ms_banks.adapter.output.persistence.entity.BankEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for BankEntity.
 */
@Repository
public interface BankJpaRepository extends JpaRepository<BankEntity, UUID> {
    
    /**
     * Checks whether a bank exists with the given code.
     */
    boolean existsByCode(String code);
    
    /**
     * Finds a bank by its code.
     */
    Optional<BankEntity> findByCode(String code);
    
    /**
     * Finds banks by country using pagination.
     */
    Page<BankEntity> findByCountry(String country, Pageable pageable);
    
    /**
     * Checks whether a bank exists with the code excluding a specific identifier.
     */
    boolean existsByCodeAndIdNot(String code, UUID id);
}

