package com.divyam.aven.ledger;

import static org.assertj.core.api.Assertions.assertThat;

import com.divyam.aven.ledger.api.CreateTransactionRequest;
import com.divyam.aven.ledger.api.EntryRequest;
import com.divyam.aven.ledger.application.LedgerService;
import com.divyam.aven.ledger.domain.EntryDirection;
import com.divyam.aven.ledger.repository.OutboxEventRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(properties = "spring.task.scheduling.enabled=false")
@Testcontainers(disabledWithoutDocker = true)
class LedgerPostgresIntegrationTest {
    @Container
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("ledger_test")
            .withUsername("aven")
            .withPassword("aven");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired LedgerService ledgerService;
    @Autowired OutboxEventRepository outbox;

    @Test
    void transactionEntriesAndOutboxArePersistedAtomicallyAndIdempotently() {
        UUID debitAccount = UUID.randomUUID();
        UUID creditAccount = UUID.randomUUID();
        CreateTransactionRequest request = new CreateTransactionRequest(List.of(
                new EntryRequest(debitAccount, new BigDecimal("25.0000"), EntryDirection.DEBIT),
                new EntryRequest(creditAccount, new BigDecimal("25.0000"), EntryDirection.CREDIT)));

        var first = ledgerService.create("integration-key", "integration-trace", request);
        var duplicate = ledgerService.create("integration-key", "integration-trace", request);

        assertThat(duplicate.getId()).isEqualTo(first.getId());
        assertThat(first.getEntries()).hasSize(2);
        assertThat(outbox.findAll()).hasSize(1);
    }
}
