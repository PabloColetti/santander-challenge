package com.santander.challenge.ms_accounts.adapter.input.rest.controller;

import com.santander.challenge.ms_accounts.adapter.input.rest.dto.request.CreateAccountRequest;
import com.santander.challenge.ms_accounts.adapter.input.rest.dto.request.UpdateAccountRequest;
import com.santander.challenge.ms_accounts.adapter.input.rest.dto.response.AccountResponse;
import com.santander.challenge.ms_accounts.application.mapper.AccountMapper;
import com.santander.challenge.ms_accounts.domain.port.input.AccountServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller that exposes CRUD operations for accounts with per-bank isolation.
 */
@Tag(name = "Accounts", description = "API for bank account management")
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
     * Creates a new account.
     */
    @Operation(
            summary = "Create account",
            description = "Creates a new bank account. The provided bankId must exist in ms-banks and the account number must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified bank does not exist",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account number already exists",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        var account = accountMapper.toDomain(request);
        var createdAccount = accountService.createAccount(account);
        var response = accountMapper.toResponse(createdAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Retrieves an account by its identifier, requiring the bankId to enforce isolation.
     */
    @Operation(
            summary = "Get account by ID",
            description = "Retrieves a specific account by UUID. Requires bankId as a query parameter to validate isolation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account found",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found or does not belong to the specified bank",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized access to the account",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @Parameter(description = "Account UUID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Bank UUID (required for isolation)", required = true)
            @RequestParam UUID bankId) {
        var account = accountService.getAccountById(id, bankId);
        var response = accountMapper.toResponse(account);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lists accounts for a specific bank. bankId is required to enforce isolation.
     */
    @Operation(
            summary = "List accounts by bank",
            description = "Lists all accounts for a bank with pagination. bankId is required to guarantee isolation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAccountsByBankId(
            @Parameter(description = "Bank UUID (required)", required = true)
            @RequestParam UUID bankId,
            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 10) Pageable pageable) {
        
        var accounts = accountService.getAccountsByBankId(bankId, pageable);
        var response = accounts.map(accountMapper::toResponse);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Updates an existing account. Requires bankId to validate ownership.
     */
    @Operation(
            summary = "Update account",
            description = "Updates an existing account. Requires bankId as a query parameter to validate ownership."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found or does not belong to the specified bank",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized access to the account",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "Account UUID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Bank UUID (required for validation)", required = true)
            @RequestParam UUID bankId,
            @Valid @RequestBody UpdateAccountRequest request) {
        var account = accountMapper.toDomain(request);
        var updatedAccount = accountService.updateAccount(id, bankId, account);
        var response = accountMapper.toResponse(updatedAccount);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Deletes an account. Requires bankId to validate ownership.
     */
    @Operation(
            summary = "Delete account",
            description = "Deletes an account and requires bankId as a query parameter to validate ownership."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Account deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found or does not belong to the specified bank",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized access to the account",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account UUID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Bank UUID (required for validation)", required = true)
            @RequestParam UUID bankId) {
        accountService.deleteAccount(id, bankId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Counts the accounts associated with a bank; used by ms-banks prior to deletion.
     */
    @Operation(
            summary = "Count accounts by bank",
            description = "Counts the number of accounts linked to a bank. Used by ms-banks before deleting a bank."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Count retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Long.class))
            )
    })
    @GetMapping("/count")
    public ResponseEntity<Long> countByBankId(
            @Parameter(description = "Bank UUID", required = true)
            @RequestParam UUID bankId) {
        Long count = accountService.countByBankId(bankId);
        return ResponseEntity.ok(count);
    }
}

