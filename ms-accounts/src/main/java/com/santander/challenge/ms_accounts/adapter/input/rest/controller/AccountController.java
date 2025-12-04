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
 * Controller REST para operaciones CRUD de cuentas.
 */
@Tag(name = "Accounts", description = "API para gestión de cuentas bancarias")
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
    @Operation(
            summary = "Crear cuenta",
            description = "Crea una nueva cuenta bancaria. El bankId debe existir en ms-banks. " +
                    "El número de cuenta debe ser único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cuenta creada exitosamente",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El banco especificado no existe",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El número de cuenta ya existe",
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
     * Obtiene una cuenta por ID.
     * Requiere bankId como query parameter para aislamiento de datos.
     */
    @Operation(
            summary = "Obtener cuenta por ID",
            description = "Obtiene la información de una cuenta específica mediante su UUID. " +
                    "Requiere bankId como query parameter para validación de aislamiento de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cuenta encontrada",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cuenta no encontrada o no pertenece al banco especificado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso no autorizado a la cuenta",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @Parameter(description = "UUID de la cuenta", required = true)
            @PathVariable UUID id,
            @Parameter(description = "UUID del banco (requerido para aislamiento)", required = true)
            @RequestParam UUID bankId) {
        var account = accountService.getAccountById(id, bankId);
        var response = accountMapper.toResponse(account);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lista cuentas de un banco específico.
     * bankId es requerido para aislamiento de datos.
     */
    @Operation(
            summary = "Listar cuentas por banco",
            description = "Lista todas las cuentas de un banco específico con paginación. " +
                    "bankId es requerido para aislamiento de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cuentas obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAccountsByBankId(
            @Parameter(description = "UUID del banco (requerido)", required = true)
            @RequestParam UUID bankId,
            @Parameter(description = "Parámetros de paginación (page, size, sort)")
            @PageableDefault(size = 10) Pageable pageable) {
        
        var accounts = accountService.getAccountsByBankId(bankId, pageable);
        var response = accounts.map(accountMapper::toResponse);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualiza una cuenta existente.
     * Requiere bankId como query parameter para validación.
     */
    @Operation(
            summary = "Actualizar cuenta",
            description = "Actualiza la información de una cuenta existente. " +
                    "Requiere bankId como query parameter para validación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cuenta actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cuenta no encontrada o no pertenece al banco especificado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso no autorizado a la cuenta",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "UUID de la cuenta", required = true)
            @PathVariable UUID id,
            @Parameter(description = "UUID del banco (requerido para validación)", required = true)
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
    @Operation(
            summary = "Eliminar cuenta",
            description = "Elimina una cuenta. Requiere bankId como query parameter para validación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Cuenta eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cuenta no encontrada o no pertenece al banco especificado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso no autorizado a la cuenta",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "UUID de la cuenta", required = true)
            @PathVariable UUID id,
            @Parameter(description = "UUID del banco (requerido para validación)", required = true)
            @RequestParam UUID bankId) {
        accountService.deleteAccount(id, bankId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Cuenta las cuentas asociadas a un banco.
     * Usado por ms-banks para validar antes de eliminar.
     */
    @Operation(
            summary = "Contar cuentas por banco",
            description = "Cuenta el número de cuentas asociadas a un banco. " +
                    "Usado por ms-banks para validar antes de eliminar un banco."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conteo exitoso",
                    content = @Content(schema = @Schema(implementation = Long.class))
            )
    })
    @GetMapping("/count")
    public ResponseEntity<Long> countByBankId(
            @Parameter(description = "UUID del banco", required = true)
            @RequestParam UUID bankId) {
        Long count = accountService.countByBankId(bankId);
        return ResponseEntity.ok(count);
    }
}

