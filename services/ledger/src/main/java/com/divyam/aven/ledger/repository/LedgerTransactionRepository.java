package com.divyam.aven.ledger.repository;

import com.divyam.aven.ledger.domain.LedgerTransaction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, UUID> {

    Optional<LedgerTransaction> findByIdempotencyKey(String idempotencyKey);

    @EntityGraph(attributePaths = "entries")
    @Query("select transaction from LedgerTransaction transaction where transaction.id = :id")
    Optional<LedgerTransaction> findWithEntriesById(UUID id);
}
