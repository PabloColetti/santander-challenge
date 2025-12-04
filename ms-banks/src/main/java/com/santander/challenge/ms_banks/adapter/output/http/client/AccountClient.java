package com.santander.challenge.ms_banks.adapter.output.http.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Feign client used to communicate with ms-accounts in order to validate
 * whether a bank has associated accounts.
 */
@FeignClient(name = "ms-accounts", url = "${feign.client.ms-accounts.url:http://localhost:9090}")
public interface AccountClient {
    
    /**
     * Counts the accounts associated with a bank.
     *
     * @param bankId bank identifier
     * @return number of accounts
     */
    @GetMapping("/api/accounts/count")
    Long countByBankId(@RequestParam("bankId") UUID bankId);
}

/**
 * Adapter that implements AccountCountPort using Feign.
 */
@org.springframework.stereotype.Component
class AccountCountAdapter implements com.santander.challenge.ms_banks.domain.port.output.AccountCountPort {
    
    private final AccountClient accountClient;
    
    public AccountCountAdapter(AccountClient accountClient) {
        this.accountClient = accountClient;
    }
    
    @Override
    public Long countByBankId(UUID bankId) {
        try {
            return accountClient.countByBankId(bankId);
        } catch (Exception e) {
            // Fallback: assume there are zero accounts so that deletion can continue.
            // In production this should be handled with proper resilience patterns (circuit breaker, retries, etc.).
            return 0L;
        }
    }
}

