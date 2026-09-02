package com.divyam.aven.reconciliation.application;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountBalanceClient {
    private final RestClient restClient;

    public AccountBalanceClient(
            RestClient.Builder builder,
            @Value("${account-service.base-url}") String accountServiceUrl) {
        this.restClient = builder.baseUrl(accountServiceUrl).build();
    }

    public BigDecimal fetch(UUID accountId) {
        BalancePayload response = restClient.get()
                .uri("/accounts/{accountId}/balance", accountId)
                .retrieve()
                .body(BalancePayload.class);
        if (response == null) throw new IllegalStateException("Account Service returned an empty balance response");
        return response.balance();
    }

    private record BalancePayload(UUID accountId, BigDecimal balance) { }
}
