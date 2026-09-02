package com.divyam.aven.ledger.repository;

import com.divyam.aven.ledger.domain.LedgerEntry;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    Page<LedgerEntry> findByAccountId(UUID accountId, Pageable pageable);
}
