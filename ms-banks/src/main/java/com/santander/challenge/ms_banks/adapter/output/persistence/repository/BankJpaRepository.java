package com.santander.challenge.ms_banks.adapter.output.persistence.repository;

import com.santander.challenge.ms_banks.adapter.output.persistence.entity.BankEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para BankEntity.
 */
@Repository
public interface BankJpaRepository extends JpaRepository<BankEntity, UUID> {
    
    /**
     * Verifica si existe un banco con el código dado.
     */
    boolean existsByCode(String code);
    
    /**
     * Busca un banco por su código.
     */
    Optional<BankEntity> findByCode(String code);
    
    /**
     * Busca bancos por país con paginación.
     */
    Page<BankEntity> findByCountry(String country, Pageable pageable);
    
    /**
     * Verifica si existe un banco con el código dado excluyendo un ID específico.
     */
    boolean existsByCodeAndIdNot(String code, UUID id);
}

