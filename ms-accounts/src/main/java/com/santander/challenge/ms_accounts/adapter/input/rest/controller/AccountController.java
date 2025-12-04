package com.santander.challenge.ms_accounts.adapter.input.rest.controller;

import com.santander.challenge.ms_accounts.adapter.input.rest.dto.request.CreateAccountRequest;
import com.santander.challenge.ms_accounts.adapter.input.rest.dto.request.UpdateAccountRequest;
import com.santander.challenge.ms_accounts.adapter.input.rest.dto.response.AccountResponse;
import com.santander.challenge.ms_accounts.application.mapper.AccountMapper;
import com.santander.challenge.ms_accounts.domain.port.input.AccountServicePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST para operaciones CRUD de cuentas.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountServicePort accountService;
    private final AccountMapper accountMapper;
    
    public AccountController(AccountServicePort accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }
    
    /**
     * Crea una nueva cuenta.
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        var account = accountMapper.toDomain(request);
        var createdAccount = accountService.createAccount(account);
        var response = accountMapper.toResponse(createdAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtiene una cuenta por ID.
     * Requiere bankId como query parameter para aislamiento de datos.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID id,
            @RequestParam UUID bankId) {
        var account = accountService.getAccountById(id, bankId);
        var response = accountMapper.toResponse(account);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lista cuentas de un banco específico.
     * bankId es requerido para aislamiento de datos.
     */
    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAccountsByBankId(
            @RequestParam UUID bankId,
            @PageableDefault(size = 10) Pageable pageable) {
        
        var accounts = accountService.getAccountsByBankId(bankId, pageable);
        var response = accounts.map(accountMapper::toResponse);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualiza una cuenta existente.
     * Requiere bankId como query parameter para validación.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable UUID id,
            @RequestParam UUID bankId,
            @Valid @RequestBody UpdateAccountRequest request) {
        var account = accountMapper.toDomain(request);
        var updatedAccount = accountService.updateAccount(id, bankId, account);
        var response = accountMapper.toResponse(updatedAccount);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Elimina una cuenta.
     * Requiere bankId como query parameter para validación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable UUID id,
            @RequestParam UUID bankId) {
        accountService.deleteAccount(id, bankId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Cuenta las cuentas asociadas a un banco.
     * Usado por ms-banks para validar antes de eliminar.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countByBankId(@RequestParam UUID bankId) {
        Long count = accountService.countByBankId(bankId);
        return ResponseEntity.ok(count);
    }
}

