package com.santander.challenge.ms_accounts.adapter.input.rest.exception;

/**
 * Códigos de error del microservicio ms-accounts.
 * Cada código identifica de manera única un tipo de error.
 */
public enum ErrorCode {
    
    // Errores de recurso no encontrado (4xx)
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Account not found"),
    BANK_NOT_FOUND("BANK_NOT_FOUND", "Bank not found"),
    
    // Errores de validación (4xx)
    ACCOUNT_VALIDATION_ERROR("ACCOUNT_VALIDATION_ERROR", "Account validation error"),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request data"),
    
    // Errores de conflicto (4xx)
    DUPLICATE_ACCOUNT_NUMBER("DUPLICATE_ACCOUNT_NUMBER", "Account number already exists"),
    
    // Errores de autorización (4xx)
    UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS", "Unauthorized access to account"),
    
    // Errores del servidor (5xx)
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service temporarily unavailable");
    
    private final String code;
    private final String description;
    
    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

